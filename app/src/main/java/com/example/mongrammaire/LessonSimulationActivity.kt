package com.example.mongrammaire

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mongrammaire.Data.Local.LessonDatabaseHelper
import com.example.mongrammaire.Model.LessonModel
import com.example.mongrammaire.Quiz.TriviaQuestion
import com.example.mongrammaire.databinding.ActivityLessonSimulationBinding
import com.example.mongrammaire.Utils.ToastHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class LessonSimulationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLessonSimulationBinding
    private var timer: CountDownTimer? = null
    
    private var simulationQuestions: List<TriviaQuestion> = emptyList()
    private var currentQuestionIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonSimulationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        generateSimulation()
        startSimulationTimer()
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener { showExitConfirmation() }
        binding.btnQuit.setOnClickListener { showExitConfirmation() }
        binding.btnFinish.setOnClickListener { finish() }
        
        binding.btnNext.setOnClickListener {
            if (binding.optionsGroup.checkedRadioButtonId == -1) {
                ToastHelper.showCustomToast(this, "Veuillez choisir une réponse")
            } else {
                checkAnswerAndMove()
            }
        }
    }

    private fun checkAnswerAndMove() {
        val selectedId = binding.optionsGroup.checkedRadioButtonId
        val selectedText = when (selectedId) {
            binding.rbOptA.id -> binding.rbOptA.text
            binding.rbOptB.id -> binding.rbOptB.text
            binding.rbOptC.id -> binding.rbOptC.text
            binding.rbOptD.id -> binding.rbOptD.text
            else -> ""
        }

        if (selectedText == simulationQuestions[currentQuestionIndex].answer) {
            score++
        }

        currentQuestionIndex++
        if (currentQuestionIndex < simulationQuestions.size) {
            displayQuestion(currentQuestionIndex)
        } else {
            finishSimulation()
        }
    }

    private fun showExitConfirmation() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Quitter la simulation ?")
            .setMessage("Votre progression sera perdue. Voulez-vous vraiment quitter ?")
            .setNegativeButton("Continuer") { d, _ -> d.dismiss() }
            .setPositiveButton("Quitter") { _, _ -> finish() }
            .show()
    }

    private fun generateSimulation() {
        lifecycleScope.launch {
            val loadedQuestions = withContext(Dispatchers.IO) {
                val dbHelper = LessonDatabaseHelper(this@LessonSimulationActivity)
                val allLessons = dbHelper.allLessons
                val allExtractedQuestions = mutableListOf<TriviaQuestion>()
                val allAnswers = mutableListOf<String>()

                // 1. Extract all [EXAMPLE] questions and answers
                for (lesson in allLessons) {
                    val extracted = extractQuestionsFromContent(lesson.content)
                    allExtractedQuestions.addAll(extracted)
                    allAnswers.addAll(extracted.map { it.answer })
                }

                // 2. For each question, generate 3 random wrong options
                allExtractedQuestions.forEach { q ->
                    val wrongOptions = allAnswers.filter { it != q.answer }.shuffled().distinct().take(3)
                    val options = (wrongOptions + q.answer).shuffled()
                    
                    // Assign options to A, B, C, D slots
                    q.optA = if (options.size > 0) options[0] else ""
                    q.optB = if (options.size > 1) options[1] else ""
                    q.optC = if (options.size > 2) options[2] else ""
                    q.optD = if (options.size > 3) options[3] else ""
                }

                allExtractedQuestions.shuffled().take(30) // Limit to 30 questions per simulation
            }

            simulationQuestions = loadedQuestions
            
            binding.loadingContainer.visibility = View.GONE
            if (simulationQuestions.isNotEmpty()) {
                binding.quizContainer.visibility = View.VISIBLE
                binding.examProgress.max = simulationQuestions.size
                displayQuestion(0)
            } else {
                ToastHelper.showCustomToast(this@LessonSimulationActivity, "Erreur : pas assez de contenu pour générer une simulation.")
                finish()
            }
        }
    }

    private fun extractQuestionsFromContent(content: String): List<TriviaQuestion> {
        val questions = mutableListOf<TriviaQuestion>()
        val pattern = Pattern.compile("\\[EXAMPLE](.*?)\\[") // Simplified match for examples between tags
        val matcher = pattern.matcher(content + "[") // Append [ to help match last tag

        while (matcher.find()) {
            val rawExample = matcher.group(1)?.trim() ?: ""
            if (rawExample.contains("-> [")) {
                val parts = rawExample.split("-> [")
                if (parts.size == 2) {
                    val questionText = parts[0].trim()
                    val answerText = parts[1].replace("]", "").trim()
                    if (questionText.isNotEmpty() && answerText.isNotEmpty()) {
                        questions.add(TriviaQuestion(questionText, "", "", "", "", answerText))
                    }
                }
            }
        }
        return questions
    }

    private fun displayQuestion(index: Int) {
        if (index >= simulationQuestions.size) return
        
        val q = simulationQuestions[index]
        binding.tvQuestion.text = q.question
        binding.rbOptA.text = q.optA
        binding.rbOptB.text = q.optB
        binding.rbOptC.text = q.optC
        binding.rbOptD.text = q.optD
        
        binding.optionsGroup.clearCheck()
        binding.examProgress.setProgress(index, true)
        
        binding.tvModeStatus.text = "SIMULATION • ${index + 1} / ${simulationQuestions.size}"
    }

    private fun finishSimulation() {
        timer?.cancel()
        binding.quizContainer.visibility = View.GONE
        binding.btnQuit.visibility = View.GONE
        binding.examProgress.setProgress(simulationQuestions.size, true)
        
        binding.resultsContainer.visibility = View.VISIBLE
        binding.tvFinalScore.text = "Score: $score / ${simulationQuestions.size}"
        binding.tvLevelEstimate.text = "Niveau estimé : ${estimateLevel(score, simulationQuestions.size)}"
    }

    private fun estimateLevel(score: Int, total: Int): String {
        val percentage = (score.toFloat() / total.toFloat()) * 100
        return when {
            percentage >= 90 -> "C1/C2 (Expert)"
            percentage >= 70 -> "B2 (Avancé)"
            percentage >= 50 -> "B1 (Intermédiaire)"
            percentage >= 30 -> "A2 (Élémentaire)"
            else -> "A1 (Débutant)"
        }
    }

    private fun startSimulationTimer() {
        val totalTime = 45 * 60 * 1000L // 45 minutes

        timer = object : CountDownTimer(totalTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                binding.tvTimer.text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                
                if (millisUntilFinished < 5 * 60 * 1000) {
                    binding.timerCard.setStrokeColor(Color.RED)
                    binding.tvTimer.setTextColor(Color.RED)
                }
            }

            override fun onFinish() {
                ToastHelper.showCustomToast(this@LessonSimulationActivity, "Temps écoulé !")
                finishSimulation()
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
