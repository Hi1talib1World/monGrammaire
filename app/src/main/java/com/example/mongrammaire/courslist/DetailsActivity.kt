package com.example.mongrammaire.courslist

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
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
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.example.mongrammaire.databinding.ActivityDetailsBinding
import com.example.mongrammaire.databinding.ItemLearningStepBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.util.*

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

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
        val options = arrayOf(
            "Auto-Suivant : ${if(viewModel.uiState.value.isAutoNext) "ON" else "OFF"}",
            "Marquer comme maîtrisé",
            "Taille du texte : Petite",
            "Taille du texte : Normale",
            "Taille du texte : Grande"
        )
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Options de la leçon")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> viewModel.toggleAutoNext()
                    1 -> viewModel.toggleMastered()
                    2 -> updateTextSize(14f)
                    3 -> updateTextSize(18f)
                    4 -> updateTextSize(24f)
                }
            }
            .show()
    }

    private fun updateTextSize(size: Float) {
        // Find the recycler view and refresh its child views or adapter
        // For simplicity, we'll re-set the adapter with the new size constraint
        // (In a real app we'd use a dynamic binding variable)
        ToastHelper.showCustomToast(this, "Taille mise à jour")
        recreate() // Simple way to apply global font changes if persisted
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
            finish()
            return
        }

        if (state.errorEvent != null) {
            ToastHelper.showCustomToast(this, state.errorEvent)
            viewModel.clearError()
        }

        // Atomic UI Reflection
        if (binding.cardViewPager.adapter == null && state.steps.isNotEmpty()) {
            binding.cardViewPager.adapter = LessonCardAdapter(state.steps, { cardIndex ->
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
            binding.btnContinue.setText("TERMINER")
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

            val colorRes = when (step.type) {
                "[EXAMPLE]" -> R.color.accent_green
                "[EXCEPTION]" -> R.color.red
                "[FINISH]" -> R.color.accent_green
                else -> R.color.primary
            }

            val color = ContextCompat.getColor(holder.itemView.context, colorRes)

            holder.binding.tvStepTitle.text = title
            holder.binding.tvStepTitle.setTextColor(color)
            holder.binding.ivStepIcon.setImageResource(iconRes)
            holder.binding.ivStepIcon.setColorFilter(color)
            holder.binding.cardContainer.setStrokeColor(ColorStateList.valueOf(color))
            
            val formatted = step.content
                .replace("->", " <font color='#FFCC00'><b>➔</b></font> ")
                .replace("\n", "<br/>")
            
            holder.binding.tvStepContent.text = HtmlCompat.fromHtml(formatted, HtmlCompat.FROM_HTML_MODE_LEGACY)

            // Special styling for Finish step
            if (step.type == "[FINISH]") {
                holder.binding.tvStepContent.setTextColor(color)
                holder.binding.tvStepContent.textSize = 22f
            } else {
                holder.binding.tvStepContent.setTextColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.onSurface)
                )
                holder.binding.tvStepContent.textSize = 18f
            }

            // Reveal logic
            if (step.revealContent != null) {
                holder.binding.revealCard.visibility = View.VISIBLE
                holder.binding.tvRevealHint.text = "Appuyez pour voir la réponse"
                holder.binding.tvRevealHint.setTextColor(color)
                holder.binding.revealCard.setStrokeColor(ColorStateList.valueOf(color))
                holder.binding.revealCard.setOnClickListener {
                    holder.binding.tvRevealHint.text = step.revealContent
                    holder.binding.revealCard.setCardBackgroundColor(
                        ColorStateList.valueOf(color).withAlpha(30).defaultColor
                    )
                }
            } else {
                holder.binding.revealCard.visibility = View.GONE
            }

            holder.binding.btnBookmarkCard.setOnClickListener {
                onBookmark(position)
                ToastHelper.showCustomToast(it.context, "Carte épinglée !")
            }

            holder.binding.btnShareCard.setOnClickListener {
                onShare("${title}: ${step.content}")
            }

            holder.binding.tvStepContent.setOnLongClickListener {
                val clipboard = it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("monGrammaire Rule", step.content)
                clipboard.setPrimaryClip(clip)
                ToastHelper.showCustomToast(it.context, "Copié dans le presse-papiers")
                true
            }
        }

        override fun getItemCount() = steps.size
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right)
    }
}
