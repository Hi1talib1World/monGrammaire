package com.example.mongrammaire

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mongrammaire.Quiz.MainGameActivity
import com.example.mongrammaire.Utils.AdManager
import com.example.mongrammaire.Utils.ProgressionManager
import com.example.mongrammaire.courslist.Cours2ListActivity
import com.example.mongrammaire.courslist.CoursListActivity
import com.example.mongrammaire.databinding.FragmentHomeBinding
import com.example.mongrammaire.horisontal_cardv.Adapter
import com.example.mongrammaire.horisontal_cardv.Model
import com.example.mongrammaire.horisontal_cardv.NAVDRAWER
import com.google.android.gms.ads.AdRequest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    private val myImageList = intArrayOf(R.drawable.n1, R.drawable.n2, R.drawable.n3, R.drawable.n4, R.drawable.n5, R.drawable.n6, R.drawable.n7, R.drawable.n8)
    private val myImageNameList = arrayOf("Niveau 1", "Niveau 2", "Niveau 3", "Niveau 4", "Niveau 5", "Niveau 6", "Niveau 7", "Niveau 8")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        ProgressionManager.init(requireContext())
        setupAd()
        setupListeners()
        observeState()
        
        val dbHelper = com.example.mongrammaire.Data.Local.LessonDatabaseHelper(requireContext())
        val recommendedId = dbHelper.recommendedLessonId
        
        // Update click listener with recommended ID
        binding.cardDailyLesson.setOnClickListener {
            val intent = Intent(context, com.example.mongrammaire.courslist.DetailsActivity::class.java)
            intent.putExtra("iId", recommendedId)
            startActivity(intent)
        }

        viewModel.updateStats(
            dbHelper.overallCompletionPercentage,
            dbHelper.masteredLessonCount,
            dbHelper.dueReviewsCount
        )

        viewModel.loadUserData()
    }

    private fun setupAd() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        AdManager.loadInterstitialAd(requireContext())
    }

    private fun setupListeners() {
        binding.btnMenu.setOnClickListener {
            val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
            drawer?.openDrawer(GravityCompat.START)
        }

        binding.listView.setOnClickListener {
            startActivity(Intent(context, CoursListActivity::class.java))
        }

        binding.listView2.setOnClickListener {
            startActivity(Intent(context, Cours2ListActivity::class.java))
        }

        binding.btnConjugaison.setOnClickListener {
            (requireActivity() as? NAVDRAWER)?.onNavigationItemSelected(
                (requireActivity().findViewById<View>(R.id.nav_view) as com.google.android.material.navigation.NavigationView)
                    .menu.findItem(R.id.nav_gallery)
            )
            // Ideally should filter by "Verbe" but for now just navigate to cours
        }

        binding.cardDictionary.setOnClickListener {
            (requireActivity() as? NAVDRAWER)?.onNavigationItemSelected(
                (requireActivity().findViewById<View>(R.id.nav_view) as com.google.android.material.navigation.NavigationView)
                    .menu.findItem(R.id.nav_home)
            )
        }

        binding.cardFavorites.setOnClickListener {
            (requireActivity() as? NAVDRAWER)?.onNavigationItemSelected(
                (requireActivity().findViewById<View>(R.id.nav_view) as com.google.android.material.navigation.NavigationView)
                    .menu.findItem(R.id.nav_slideshow)
            )
        }
        
        binding.profileImage.setOnClickListener {
            com.example.mongrammaire.Utils.ToastHelper.showCustomToast(requireContext(), getString(R.string.profil_bientot))
        }

        binding.btnStartQuiz.setOnClickListener {
            AdManager.showInterstitial(requireActivity()) {
                startActivity(Intent(context, MainGameActivity::class.java))
            }
        }

        binding.btnStartReviews.setOnClickListener {
            com.example.mongrammaire.Utils.ToastHelper.showCustomToast(requireContext(), getString(R.string.revisions_bientot))
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    renderUi(state)
                }
            }
        }
    }

    private fun renderUi(state: HomeUiState) {
        // Point 3: Feedback via alpha/loading
        binding.root.alpha = if (state.isLoading) 0.6f else 1.0f
        
        binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background))
        
        // Point 4: Dynamic rendering with more if-else logic
        if (state.seasonalMessage != null) {
            binding.textView.text = "${state.seasonalMessage}\n${state.greeting}, ${state.userName} ! (${state.userTitle})"
        } else {
            binding.textView.text = "${state.greeting}, ${state.userName} ! (${state.userTitle})"
        }
        binding.subWelcome.text = "${state.encouragement} • ${state.learningVelocity}"
        
        binding.streakValue.text = state.streak.toString()
        binding.scoreValue.text = state.score.toString()
        
        // Dynamic level description
        val levelName = if (state.level >= 7) "EXPERT" 
                       else if (state.level >= 4) "INTERMÉDIAIRE"
                       else "DÉBUTANT"
        binding.levelValue.text = "NIVEAU ${state.level} • $levelName (${state.performanceTier})"
        
        // Apply logic-based color to the level label
        try {
            binding.levelValue.setTextColor(android.graphics.Color.parseColor(state.statusColor))
        } catch (e: Exception) {
            binding.levelValue.setTextColor(android.graphics.Color.WHITE)
        }

        binding.mainProgress.setProgress(state.overallProgress, true)
        
        // Logic for progress bar color
        if (state.overallProgress > 80) {
            binding.mainProgress.setIndicatorColor(ContextCompat.getColor(requireContext(), R.color.accent_green))
        } else if (state.overallProgress > 40) {
            binding.mainProgress.setIndicatorColor(android.graphics.Color.YELLOW)
        } else {
            binding.mainProgress.setIndicatorColor(android.graphics.Color.WHITE)
        }

        // Logic for Ranking and XP Multiplier
        if (state.ranking != "En attente") {
            binding.tvRanking.visibility = View.VISIBLE
            binding.tvRanking.text = "Classement : ${state.ranking}"
        } else {
            binding.tvRanking.visibility = View.GONE
        }

        if (state.xpMultiplier > 1.0f) {
            binding.chipMultiplier.visibility = View.VISIBLE
            binding.chipMultiplier.text = "x${state.xpMultiplier} XP"
        } else {
            binding.chipMultiplier.visibility = View.GONE
        }
        
        // Fetch and show the actual recommended lesson title
        val dbHelper = com.example.mongrammaire.Data.Local.LessonDatabaseHelper(requireContext())
        val recommendedId = dbHelper.recommendedLessonId
        val lessons = dbHelper.allLessons
        val recommendedLesson = lessons.find { it.id == recommendedId }
        
        if (recommendedLesson != null) {
            binding.tvDailyLessonTitle.text = recommendedLesson.title
            binding.tvDailyLessonDesc.text = recommendedLesson.description
        }

        // Toggle card visibility based on completion
        if (state.overallProgress >= 100) {
            binding.cardDailyLesson.visibility = View.GONE
            binding.subWelcome.text = "Félicitations ! Vous avez tout terminé ! 🎓"
        } else {
            binding.cardDailyLesson.visibility = View.VISIBLE
        }

        binding.tvMasteredCount.text = getString(R.string.lessons_completed, state.masteredLessons)
        binding.tvAccuracyScore.text = getString(R.string.accuracy_format, state.accuracy)
        
        // Show completion forecast if progress > 0
        if (state.overallProgress > 0 && state.overallProgress < 100) {
            binding.tvAccuracyWarning.visibility = View.VISIBLE
            binding.tvAccuracyWarning.text = state.completionForecast
            binding.tvAccuracyWarning.setTextColor(ContextCompat.getColor(requireContext(), R.color.accent_gold))
        } else {
            // Re-apply normal accuracy logic if forecast isn't shown
            if (state.accuracy < 60 && state.accuracy > 0) {
                binding.tvAccuracyWarning.visibility = View.VISIBLE
                binding.tvAccuracyWarning.text = "Pensez à réviser vos leçons !"
                binding.tvAccuracyWarning.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            } else {
                binding.tvAccuracyWarning.visibility = View.GONE
            }
        }

        // Added logic for Study Tip visibility
        if (state.studyTip.isNotEmpty()) {
            binding.cardStudyTip.visibility = View.VISIBLE
            binding.tvStudyTip.text = state.studyTip
        } else {
            binding.cardStudyTip.visibility = View.GONE
        }

        // Recommended activity highlighting logic
        if (state.recommendedActivity == "Quiz") {
            binding.cardDailyQuiz.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.accent_gold)))
            binding.cardDailyQuiz.setStrokeWidth(4)
        } else if (state.recommendedActivity == "Dictionnaire") {
            binding.cardDictionary.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.accent_gold)))
            binding.cardDictionary.setStrokeWidth(4)
        } else {
            binding.cardDailyQuiz.setStrokeWidth(0)
            binding.cardDictionary.setStrokeWidth(0)
        }

        // Logic for weekend specific styling
        if (state.isWeekend) {
            binding.statsCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.tertiary))
        } else {
            binding.statsCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary))
        }

        // Added logic for Rating Prompt
        if (state.showRatingPrompt) {
            binding.cardRating.visibility = View.VISIBLE
            binding.btnRate.setOnClickListener {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, android.net.Uri.parse("market://details?id=" + requireContext().packageName)))
                } catch (e: Exception) {
                    startActivity(Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://play.google.com/store/apps/details?id=" + requireContext().packageName)))
                }
            }
        } else {
            binding.cardRating.visibility = View.GONE
        }

        // Logic for Next Reward message
        if (state.level < 8) {
            binding.tvAccuracyWarning.visibility = View.VISIBLE
            binding.tvAccuracyWarning.text = "Prochaine récompense : ${state.nextReward}"
            binding.tvAccuracyWarning.setTextColor(ContextCompat.getColor(requireContext(), R.color.accent_gold))
        }

        if (!state.isLoading) {
            setupRecyclerView()
            startAnimations()
        }
    }

    private fun setupRecyclerView() {
        if (binding.recycler.adapter == null) {
            val list = myImageList.indices.map { i ->
                Model().apply {
                    name = myImageNameList[i]
                    image_drawable = myImageList[i]
                }
            }
            binding.recycler.adapter = Adapter(context, ArrayList(list))
            binding.recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun startAnimations() {
        binding.statsCard.apply {
            alpha = 0f
            translationY = 20f
            animate().alpha(1f).translationY(0f).setDuration(600).start()
        }
        binding.cardDailyQuiz.apply {
            alpha = 0f
            translationY = 20f
            animate().alpha(1f).translationY(0f).setDuration(600).setStartDelay(200).start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
