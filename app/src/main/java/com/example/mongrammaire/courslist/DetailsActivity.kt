package com.example.mongrammaire.courslist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.mongrammaire.Quiz.MainGameActivity
import com.example.mongrammaire.R
import com.example.mongrammaire.databinding.ActivityDetailsBinding
import com.example.mongrammaire.databinding.ItemLearningStepBinding
import kotlinx.coroutines.launch

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rawContent = intent.getStringExtra("iContent") ?: intent.getStringExtra("iDescTv")
        viewModel.loadContent(rawContent)

        setupViewPager()
        setupListeners()
        observeState()
    }

    private fun setupViewPager() {
        // We use a transformer for Point 3 (Transitions)
        val transformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
            addTransformer { page, position ->
                val r = 1 - Math.abs(position)
                page.scaleY = 0.85f + r * 0.15f
                page.alpha = 0.5f + r * 0.5f
            }
        }
        binding.cardViewPager.setPageTransformer(transformer)
        binding.cardViewPager.isUserInputEnabled = false // Force button usage for step-by-step
    }

    private fun setupListeners() {
        binding.btnContinue.setOnClickListener {
            viewModel.nextStep()
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    renderUi(state)
                }
            }
        }
    }

    private fun renderUi(state: DetailsUiState) {
        if (state.isFinished) {
            startActivity(Intent(this, MainGameActivity::class.java))
            finish()
            return
        }

        // Point 4: Dynamic data rendering
        if (binding.cardViewPager.adapter == null && state.steps.isNotEmpty()) {
            binding.cardViewPager.adapter = LessonCardAdapter(state.steps)
        }

        binding.cardViewPager.setCurrentItem(state.currentStepIndex, true)
        binding.learningProgress.setProgress(state.progress, true)

        if (state.currentStepIndex == state.steps.size - 1) {
            binding.btnContinue.setText(R.string.quiz)
        } else {
            binding.btnContinue.setText("CONTINUER")
        }
    }

    class LessonCardAdapter(private val steps: List<LearningStep>) :
        RecyclerView.Adapter<LessonCardAdapter.ViewHolder>() {

        class ViewHolder(val binding: ItemLearningStepBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val b = ItemLearningStepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(b)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val step = steps[position]
            
            val (title, icon) = when (step.type) {
                "[EXAMPLE]" -> "Exemple" to R.drawable.ic_play
                "[EXCEPTION]" -> "Attention !" to R.drawable.star
                else -> "La Règle" to R.drawable.bookstack
            }

            holder.binding.tvStepTitle.text = title
            holder.binding.ivStepIcon.setImageResource(icon)
            
            val formatted = step.content
                .replace("->", " ➔ ")
                .replace("\n", "<br/>")
            
            holder.binding.tvStepContent.text = HtmlCompat.fromHtml(formatted, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        override fun getItemCount() = steps.size
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right)
    }
}
