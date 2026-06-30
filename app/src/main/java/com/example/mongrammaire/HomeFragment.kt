package com.example.mongrammaire

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.btnStartQuiz.setOnClickListener {
            AdManager.showInterstitial(requireActivity()) {
                startActivity(Intent(context, MainGameActivity::class.java))
            }
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
        
        // Point 4: Dynamic rendering
        binding.textView.text = "Bonjour, ${state.userName} !"
        binding.streakValue.text = state.streak.toString()
        binding.scoreValue.text = state.score.toString()
        binding.levelValue.text = "${state.level}/8"
        
        // Update header progress if we can find the bar (assuming it's in the layout)
        // binding.progressIndicator.setProgress(state.overallProgress, true)

        if (!state.isLoading) {
            setupRecyclerView()
            startAnimations()
        }
    }

    private fun setupRecyclerView() {
        if (binding.recycler.adapter == null) {
            val list = ArrayList<Model>()
            for (i in 0 until myImageList.size) {
                val m = Model()
                m.name = myImageNameList[i]
                m.image_drawable = myImageList[i]
                list.add(m)
            }
            binding.recycler.adapter = Adapter(context, list)
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
