package com.example.mongrammaire

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mongrammaire.Quiz.TriviaQuestion
import com.example.mongrammaire.Quiz.TriviaQuizHelper
import com.example.mongrammaire.databinding.ActivityTcfModuleBinding
import com.example.mongrammaire.Utils.ToastHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TcfModuleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTcfModuleBinding
    private var moduleName: String? = null
    
    private var questions: List<TriviaQuestion> = emptyList()
    private var currentQuestionIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTcfModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        moduleName = intent.getStringExtra("MODULE_NAME")

        setupUI()
        loadModuleQuestions()
    }

    private fun setupUI() {
        binding.tvModuleTitle.text = moduleName ?: "Entraînement TCF"
        
        binding.btnBack.setOnClickListener { showExitConfirmation() }
        
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

        if (selectedText == questions[currentQuestionIndex].answer) {
            score++
        }

        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            displayQuestion(currentQuestionIndex)
        } else {
            finishTraining()
        }
    }

    private fun showExitConfirmation() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Quitter l'entraînement ?")
            .setMessage("Voulez-vous vraiment quitter cette session d'entraînement ?")
            .setNegativeButton("Continuer") { d, _ -> d.dismiss() }
            .setPositiveButton("Quitter") { _, _ -> finish() }
            .show()
    }

    private fun loadModuleQuestions() {
        lifecycleScope.launch {
            val loadedQuestions = withContext(Dispatchers.IO) {
                val db = TriviaQuizHelper(this@TcfModuleActivity)
                db.allOfTheQuestions.shuffled().take(10)
            }

            questions = loadedQuestions
            
            binding.loadingContainer.visibility = View.GONE
            if (questions.isNotEmpty()) {
                binding.quizContainer.visibility = View.VISIBLE
                displayQuestion(0)
            } else {
                ToastHelper.showCustomToast(this@TcfModuleActivity, "Aucune question disponible pour ce module.")
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
        binding.tvProgress.text = "Question ${index + 1} / ${questions.size}"
    }

    private fun finishTraining() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Entraînement terminé")
            .setMessage("Votre score : $score / ${questions.size}\n\nContinuez à vous entraîner pour améliorer votre niveau !")
            .setCancelable(false)
            .setPositiveButton("Terminer") { _, _ -> finish() }
            .show()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right)
    }
}
