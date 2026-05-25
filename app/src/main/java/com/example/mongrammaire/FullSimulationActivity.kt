package com.example.mongrammaire

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mongrammaire.Quiz.TriviaQuestion
import com.example.mongrammaire.Quiz.TriviaQuizHelper
import com.example.mongrammaire.databinding.ActivityFullSimulationBinding
import com.example.mongrammaire.Utils.ToastHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit

class FullSimulationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullSimulationBinding
    private var isGlobalSimulation: Boolean = false
    private var timer: CountDownTimer? = null
    
    private var questions: List<TriviaQuestion> = emptyList()
    private var currentQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullSimulationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isGlobalSimulation = intent.getBooleanExtra("IS_GLOBAL_SIMULATION", false)

        setupUI()
        loadSimulationQuestions()
        startExamTimer()
    }

    private fun setupUI() {
        binding.tvModeStatus.text = if (isGlobalSimulation) "Mode : Simulation Globale" else "Mode : Entraînement"
        
        binding.btnBack.setOnClickListener { showExitConfirmation() }
        binding.btnQuit.setOnClickListener { showExitConfirmation() }
        
        binding.btnNext.setOnClickListener {
            if (binding.optionsGroup.checkedRadioButtonId == -1) {
                ToastHelper.showCustomToast(this, "Veuillez choisir une réponse")
            } else {
                moveToNextQuestion()
            }
        }
    }

    private fun showExitConfirmation() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Abandonner l'examen ?")
            .setMessage("Toute progression non soumise sera perdue. Voulez-vous vraiment quitter ?")
            .setNegativeButton("Continuer") { d, _ -> d.dismiss() }
            .setPositiveButton("Quitter") { _, _ -> finish() }
            .show()
    }

    private fun loadSimulationQuestions() {
        lifecycleScope.launch {
            // Simulation d'un chargement asynchrone depuis la DB
            val loadedQuestions = withContext(Dispatchers.IO) {
                val db = TriviaQuizHelper(this@FullSimulationActivity)
                val all = db.allOfTheQuestions
                
                if (isGlobalSimulation) {
                    // Pour une simulation globale, on prend 40 questions mixées
                    all.shuffled().take(40)
                } else {
                    // Sinon on en prend 10
                    all.shuffled().take(10)
                }
            }

            questions = loadedQuestions
            
            // Transition d'affichage
            binding.loadingContainer.visibility = View.GONE
            if (questions.isNotEmpty()) {
                binding.quizContainer.visibility = View.VISIBLE
                displayQuestion(0)
            } else {
                ToastHelper.showCustomToast(this@FullSimulationActivity, "Erreur : aucune question trouvée.")
                finish()
            }
        }
    }

    private fun displayQuestion(index: Int) {
        if (index >= questions.size) return
        
        val q = questions[index]
        binding.tvQuestion.text = q.question
        binding.rbOptA.text = q.optA
        binding.rbOptB.text = q.optB
        binding.rbOptC.text = q.optC
        binding.rbOptD.text = q.optD
        
        binding.optionsGroup.clearCheck()
        
        // Update header info (optionnel)
        val progressText = "${index + 1} / ${questions.size}"
        binding.tvModeStatus.text = if (isGlobalSimulation) "Simulation Globale • $progressText" else "Entraînement • $progressText"
    }

    private fun moveToNextQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            displayQuestion(currentQuestionIndex)
        } else {
            finishExam()
        }
    }

    private fun finishExam() {
        ToastHelper.showCustomToast(this, "Examen terminé ! Félicitations.")
        finish()
    }

    private fun startExamTimer() {
        val totalTime = if (isGlobalSimulation) 90 * 60 * 1000L else 20 * 60 * 1000L

        timer = object : CountDownTimer(totalTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                
                binding.tvTimer.text = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
                
                if (millisUntilFinished < 5 * 60 * 1000) {
                    binding.timerCard.setStrokeColor(Color.RED)
                    binding.tvTimer.setTextColor(Color.RED)
                }
            }

            override fun onFinish() {
                ToastHelper.showCustomToast(this@FullSimulationActivity, "Temps écoulé !")
                finishExam()
            }
        }.start()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
