package com.example.mongrammaire.Quiz

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mongrammaire.R
import com.example.mongrammaire.Utils.ProgressionManager
import com.example.mongrammaire.databinding.ActivityMainGameBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class MainGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainGameBinding
    private val viewModel: QuizViewModel by viewModels()
    private var feedbackDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ProgressionManager.init(this)
        
        setupListeners()
        observeState()
        
        viewModel.startQuiz(this)
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { showExitConfirmation() }

        val optionButtons = listOf(binding.buttonA, binding.buttonB, binding.buttonC, binding.buttonD)
        optionButtons.forEach { button ->
            button.setOnClickListener {
                viewModel.selectOption(button.text.toString())
            }
        }

        binding.btnCheck.setOnClickListener {
            viewModel.checkAnswer()
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

    /**
     * Point 3 & 4: Dynamic rendering and Micro-feedback logic.
     */
    private fun renderUi(state: QuizUiState) {
        // Handle Loading State (Point 3)
        binding.questionScroll.visibility = if (state.isLoading) View.GONE else View.VISIBLE
        binding.optionsContainer.visibility = if (state.isLoading) View.GONE else View.VISIBLE
        // Assume a ProgressBar exists in the layout or use the footer
        binding.btnCheck.isEnabled = state.selectedOption != null && !state.isLoading

        if (state.errorMessage != null) {
            MaterialAlertDialogBuilder(this)
                .setTitle("Erreur")
                .setMessage(state.errorMessage)
                .setPositiveButton("Réessayer") { _, _ -> viewModel.startQuiz(this) }
                .setNegativeButton("Quitter") { _, _ -> finish() }
                .show()
            return
        }

        if (state.isFinished) {
            startActivity(Intent(this, GameWon::class.java))
            finish()
            return
        }

        // Render Question Data (Point 4)
        val currentQuestion = state.questions.getOrNull(state.currentQuestionIndex)
        currentQuestion?.let { q ->
            binding.triviaQuestion.text = q.question
            binding.buttonA.text = q.optA
            binding.buttonB.text = q.optB
            binding.buttonC.text = q.optC
            binding.buttonD.text = q.optD
            
            updateOptionStyles(state.selectedOption)
        }

        // Update Progress & Timer
        val progress = if (state.questions.isNotEmpty()) {
            ((state.currentQuestionIndex).toFloat() / state.questions.size * 100).toInt()
        } else 0
        binding.quizProgress.setProgress(progress, true)
        binding.timeText.text = "${state.timerValue}s"

        // Handle Feedback (Point 3)
        handleFeedback(state.feedbackState, currentQuestion)
    }

    private fun updateOptionStyles(selected: String?) {
        val buttons = listOf(binding.buttonA, binding.buttonB, binding.buttonC, binding.buttonD)
        buttons.forEach { btn ->
            if (btn.text == selected) {
                btn.setStrokeColor(ContextCompat.getColorStateList(this, R.color.primary))
                btn.setStrokeWidth(resources.getDimensionPixelSize(R.dimen.card_margin))
                btn.setBackgroundColor(ContextCompat.getColor(this, R.color.primaryContainer))
            } else {
                btn.setStrokeColor(ContextCompat.getColorStateList(this, R.color.outline))
                btn.setStrokeWidth(resources.getDimensionPixelSize(R.dimen.card_margin) / 2)
                btn.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

    private fun handleFeedback(feedback: FeedbackState, question: TriviaQuestion?) {
        if (feedback == FeedbackState.NONE) {
            feedbackDialog?.dismiss()
            feedbackDialog = null
            return
        }

        if (feedbackDialog == null && question != null) {
            when (feedback) {
                FeedbackState.CORRECT -> showFeedbackDialog(R.layout.dialog_correct, null)
                FeedbackState.WRONG -> showFeedbackDialog(R.layout.dialog_wrong, question.answer)
                FeedbackState.TIME_UP -> showFeedbackDialog(R.layout.dialog_wrong, "Temps écoulé ! La réponse était : ${question.answer}")
                else -> {}
            }
        }
    }

    private fun showFeedbackDialog(layoutId: Int, correctAnswer: String?) {
        feedbackDialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(layoutId)
            setCancelable(false)
            
            correctAnswer?.let {
                findViewById<TextView>(R.id.correctAnswerText)?.text = it
            }

            findViewById<View>(R.id.dialogNext)?.setOnClickListener {
                viewModel.nextQuestion()
                dismiss()
            }
            findViewById<View>(R.id.dialogRetry)?.setOnClickListener {
                viewModel.nextQuestion()
                dismiss()
            }

            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val lp = WindowManager.LayoutParams().apply {
                    copyFrom(attributes)
                    width = WindowManager.LayoutParams.MATCH_PARENT
                    height = WindowManager.LayoutParams.WRAP_CONTENT
                    gravity = Gravity.BOTTOM
                }
                attributes = lp
            }
            show()
        }
    }

    private fun showExitConfirmation() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Quitter le quiz ?")
            .setMessage("Votre progression pour ce quiz sera perdue.")
            .setPositiveButton("Quitter") { _, _ -> finish() }
            .setNegativeButton("Continuer", null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        feedbackDialog?.dismiss()
    }
}
