package com.example.mongrammaire

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mongrammaire.Utils.ProgressionManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val userName: String = "Apprenant",
    val streak: Int = 0,
    val score: Int = 0,
    val level: Int = 1,
    val isLoading: Boolean = true,
    val overallProgress: Int = 0,
    val masteredLessons: Int = 0,
    val accuracy: Int = 0,
    val dueReviews: Int = 0,
    val dailyLessonTitle: String = "Le Passé Composé"
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadUserData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Mocking data fetch delay (Point 2)
            delay(800)

            val name = try {
                val user = FirebaseAuth.getInstance().currentUser
                user?.displayName ?: "Ami"
            } catch (e: Exception) {
                "Apprenant"
            }
            
            _uiState.value = HomeUiState(
                userName = name,
                streak = ProgressionManager.getStreak(),
                score = ProgressionManager.getUserScore(),
                level = ProgressionManager.getUnlockedLevel(),
                isLoading = false,
                overallProgress = 0,
                masteredLessons = 0,
                accuracy = 85 // Mocking for now
            )
        }
    }

    fun updateStats(progress: Int, mastered: Int, reviews: Int) {
        _uiState.value = _uiState.value.copy(
            overallProgress = progress,
            masteredLessons = mastered,
            dueReviews = reviews
        )
    }
}
