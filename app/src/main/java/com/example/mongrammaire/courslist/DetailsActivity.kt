package com.example.mongrammaire.courslist

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.mongrammaire.Data.LessonRepositoryImpl
import com.example.mongrammaire.Quiz.MainGameActivity
import com.example.mongrammaire.R
import com.example.mongrammaire.Utils.ToastHelper
import com.example.mongrammaire.databinding.ActivityDetailsBinding
import com.example.mongrammaire.databinding.ItemLearningStepBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.util.*

class DetailsActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityDetailsBinding
    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    // Pillar 1: Initializing with real Repository
    private val viewModel: DetailsViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DetailsViewModel(LessonRepositoryImpl(applicationContext)) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tts = TextToSpeech(this, this)

        val lessonId = intent.getIntExtra("iId", -1)
        val rawContent = intent.getStringExtra("iContent") ?: intent.getStringExtra("iDescTv")
        
        viewModel.initLesson(lessonId, rawContent)

        setupViewPager()
        setupListeners()
        observeState()
    }

    private fun setupViewPager() {
        val transformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
            addTransformer { page, position ->
                val r = 1 - Math.abs(position)
                page.scaleY = 0.85f + r * 0.15f
                page.alpha = 0.5f + r * 0.5f
            }
        }
        binding.cardViewPager.setPageTransformer(transformer)
        binding.cardViewPager.isUserInputEnabled = true
        
        binding.cardViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.updateStepIndex(position)
            }
        })
    }

    private fun setupListeners() {
        binding.btnContinue.setOnClickListener {
            viewModel.nextStep()
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnOptions.setOnClickListener {
            showOptionsDialog()
        }
    }

    private fun showOptionsDialog() {
        val options = arrayOf("Vitesse TTS (0.5x)", "Vitesse TTS (1.0x)", "Vitesse TTS (1.5x)", "Vitesse TTS (2.0x)", 
                              "Auto-Suivant : ${if(viewModel.uiState.value.isAutoNext) "ON" else "OFF"}",
                              "Marquer comme maîtrisé")
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Options de la leçon")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> viewModel.updateTtsSpeed(0.5f)
                    1 -> viewModel.updateTtsSpeed(1.0f)
                    2 -> viewModel.updateTtsSpeed(1.5f)
                    3 -> viewModel.updateTtsSpeed(2.0f)
                    4 -> viewModel.toggleAutoNext()
                    5 -> viewModel.toggleMastered()
                }
            }
            .show()
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

    /**
     * Pillar 3: Reactive UI & Interaction Interlocking
     */
    private fun renderUi(state: DetailsUiState) {
        if (state.isFinished) {
            startActivity(Intent(this, MainGameActivity::class.java))
            finish()
            return
        }

        if (state.errorEvent != null) {
            ToastHelper.showCustomToast(this, state.errorEvent)
            viewModel.clearError()
        }

        // Atomic UI Reflection
        if (binding.cardViewPager.adapter == null && state.steps.isNotEmpty()) {
            binding.cardViewPager.adapter = LessonCardAdapter(state.steps, { text ->
                if (isTtsReady) {
                    tts?.setSpeechRate(state.ttsSpeed)
                    tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }, { cardIndex ->
                viewModel.bookmarkCard(cardIndex)
            }, { text ->
                shareContent(text)
            })
        }

        // Pillar 3: Component interlocking (Disable UI while saving)
        binding.btnContinue.isEnabled = !state.isSaving
        binding.btnContinue.alpha = if (state.isSaving) 0.5f else 1.0f
        
        binding.cardViewPager.setCurrentItem(state.currentStepIndex, true)
        binding.learningProgress.setProgress(state.progress, true)

        if (state.currentStepIndex == state.steps.size - 1) {
            binding.btnContinue.setText(R.string.quiz)
        } else {
            binding.btnContinue.setText("CONTINUER")
        }
    }

    private fun shareContent(text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(Intent.createChooser(intent, "Partager avec"))
    }

    class LessonCardAdapter(
        private val steps: List<LearningStep>,
        private val onListen: (String) -> Unit,
        private val onBookmark: (Int) -> Unit,
        private val onShare: (String) -> Unit
    ) : RecyclerView.Adapter<LessonCardAdapter.ViewHolder>() {

        class ViewHolder(val binding: ItemLearningStepBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val b = ItemLearningStepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(b)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val step = steps[position]
            
            val iconRes = when (step.type) {
                "[EXAMPLE]" -> R.drawable.ic_play
                "[EXCEPTION]" -> R.drawable.star
                "[FINISH]" -> R.drawable.ic_check_circle
                else -> R.drawable.bookstack
            }

            val title = when (step.type) {
                "[EXAMPLE]" -> "Exemple"
                "[EXCEPTION]" -> "Attention !"
                "[FINISH]" -> "Terminé !"
                else -> "La Règle"
            }

            holder.binding.tvStepTitle.text = title
            holder.binding.ivStepIcon.setImageResource(iconRes)
            
            val formatted = step.content
                .replace("->", " ➔ ")
                .replace("\n", "<br/>")
            
            holder.binding.tvStepContent.text = HtmlCompat.fromHtml(formatted, HtmlCompat.FROM_HTML_MODE_LEGACY)

            // Reveal logic
            if (step.revealContent != null) {
                holder.binding.revealCard.visibility = View.VISIBLE
                holder.binding.tvRevealHint.text = "Appuyez pour voir la réponse"
                holder.binding.revealCard.setOnClickListener {
                    holder.binding.tvRevealHint.text = step.revealContent
                    holder.binding.revealCard.setCardBackgroundColor(
                        ContextCompat.getColor(it.context, R.color.primaryContainer)
                    )
                }
            } else {
                holder.binding.revealCard.visibility = View.GONE
            }

            holder.binding.btnListen.setOnClickListener {
                onListen(step.content + (step.revealContent ?: ""))
            }

            holder.binding.btnBookmarkCard.setOnClickListener {
                onBookmark(position)
                ToastHelper.showCustomToast(it.context, "Carte épinglée !")
            }

            holder.binding.btnShareCard.setOnClickListener {
                onShare("${title}: ${step.content}")
            }
        }

        override fun getItemCount() = steps.size
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.FRENCH)
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                isTtsReady = true
            }
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right)
    }
}
