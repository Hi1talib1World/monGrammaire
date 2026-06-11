package com.example.mongrammaire.Quiz

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mongrammaire.Utils.ProgressionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * State representing the Quiz UI at any given time.
 */
data class QuizUiState(
    val isLoading: Boolean = true,
    val questions: List<TriviaQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedOption: String? = null,
    val feedbackState: FeedbackState = FeedbackState.NONE,
    val timerValue: Int = 20,
    val score: Int = 0,
    val isFinished: Boolean = false,
    val errorMessage: String? = null
)

enum class FeedbackState { NONE, CORRECT, WRONG, TIME_UP }

class QuizViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private var countdownJob: kotlinx.coroutines.Job? = null

    /**
     * Initializes the quiz by fetching questions from the database.
     * Implements Point 2 (Async mocking) and Point 3 (Loading state).
     */
    fun startQuiz(context: Context) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Mocking a realistic backend/DB delay (Point 2)
            delay(1500)

            try {
                val db = TriviaQuizHelper(context)
                val allQuestions = db.allOfTheQuestions
                if (allQuestions.isEmpty()) {
                    db.allQuestion() // Seed if empty
                }
                
                val quizSet = db.allOfTheQuestions.shuffled().take(10)
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    questions = quizSet,
                    currentQuestionIndex = 0
                )
                startTimer()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Erreur lors du chargement des questions."
                )
            }
        }
    }

    private fun startTimer() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (i in 20 downTo 0) {
                _uiState.value = _uiState.value.copy(timerValue = i)
                delay(1000)
                if (i == 0 && _uiState.value.feedbackState == FeedbackState.NONE) {
                    onTimeUp()
                }
            }
        }
    }

    fun selectOption(option: String) {
        if (_uiState.value.feedbackState == FeedbackState.NONE) {
            _uiState.value = _uiState.value.copy(selectedOption = option)
        }
    }

    /**
     * Point 2: Main interaction logic.
     */
    fun checkAnswer() {
        val state = _uiState.value
        val currentQuestion = state.questions.getOrNull(state.currentQuestionIndex) ?: return
        val isCorrect = state.selectedOption == currentQuestion.answer

        countdownJob?.cancel()

        if (isCorrect) {
            ProgressionManager.addScore(10)
            _uiState.value = state.copy(
                feedbackState = FeedbackState.CORRECT,
                score = state.score + 10
            )
        } else {
            _uiState.value = state.copy(feedbackState = FeedbackState.WRONG)
        }
    }

    private fun onTimeUp() {
        _uiState.value = _uiState.value.copy(feedbackState = FeedbackState.TIME_UP)
    }

    fun nextQuestion() {
        val state = _uiState.value
        if (state.currentQuestionIndex < state.questions.size - 1) {
            _uiState.value = state.copy(
                currentQuestionIndex = state.currentQuestionIndex + 1,
                selectedOption = null,
                feedbackState = FeedbackState.NONE,
                timerValue = 20
            )
            startTimer()
        } else {
            finishQuiz()
        }
    }

    private fun finishQuiz() {
        ProgressionManager.unlockNextLevel(_uiState.value.score)
        _uiState.value = _uiState.value.copy(isFinished = true)
    }
}
