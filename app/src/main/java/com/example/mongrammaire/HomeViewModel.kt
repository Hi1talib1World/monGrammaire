package com.example.mongrammaire

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mongrammaire.Utils.ProgressionManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
    val dailyLessonTitle: String = "Le Passé Composé",
    val encouragement: String = "",
    val greeting: String = "",
    val performanceTier: String = "Bronze",
    val statusColor: String = "#2C3E50", // Default primary
    val specialBadge: String? = null,
    val isWeekend: Boolean = false,
    val studyTip: String = "",
    val completionForecast: String = "",
    val userTitle: String = "Apprenti",
    val learningVelocity: String = "Stable",
    val dailyGoalProgress: Float = 0f,
    val seasonalMessage: String? = null,
    val ranking: String = "Débutant",
    val nextReward: String = "100 XP",
    val xpMultiplier: Float = 1.0f,
    val showRatingPrompt: Boolean = false,
    val recommendedActivity: String = "Quiz"
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
            
            val streak = ProgressionManager.getStreak()
            val score = ProgressionManager.getUserScore()
            val level = ProgressionManager.getUnlockedLevel()

            // Added conditional logic for greeting based on time
            val calendar = java.util.Calendar.getInstance()
            val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
            val timeGreeting = if (hour < 12) "Bonjour" else if (hour < 18) "Bon après-midi" else "Bonsoir"

            // Added conditional logic for encouragement based on streak and progress
            val quote = if (streak > 5) {
                "Incroyable ! Une série de $streak jours ! 🔥"
            } else if (score > 1000) {
                "Vous devenez un véritable expert ! 🏆"
            } else if (level > 1) {
                "Continuez comme ça, vous progressez vite !"
            } else {
                "Prêt pour votre leçon du jour ?"
            }

            // Added conditional logic for performance tier
            val (tier, color) = if (score > 5000) {
                "Légende" to "#FFD700" // Gold
            } else if (score > 2000) {
                "Diamant" to "#B9F2FF" // Diamond
            } else if (score > 1000) {
                "Platine" to "#E5E4E2" // Platinum
            } else if (score > 500) {
                "Or" to "#FFCC00" // Gold
            } else {
                "Bronze" to "#CD7F32" // Bronze
            }

            // Added logic for weekend detection
            val dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK)
            val isWeekend = dayOfWeek == java.util.Calendar.SATURDAY || dayOfWeek == java.util.Calendar.SUNDAY

            val currentAccuracy = 85 // Mocking or fetch from somewhere

            // Added conditional study tips based on level and accuracy
            val tip = if (currentAccuracy < 50 && currentAccuracy > 0) {
                "Astuce : Prenez le temps de bien lire chaque règle avant l'exemple."
            } else if (level < 3) {
                "Astuce : La régularité est plus importante que la quantité !"
            } else if (streak > 7) {
                "Astuce : Essayez le mode Défi Éclair pour booster votre rapidité."
            } else {
                "Astuce : Les favoris sont parfaits pour réviser les points difficiles."
            }

            val currentMastered = _uiState.value.masteredLessons
            val currentProgress = _uiState.value.overallProgress

            // Added conditional logic for User Title
            val title = if (level >= 8) {
                "Maître de la Langue"
            } else if (level >= 5) {
                "Grammairien Avancé"
            } else if (score > 2000) {
                "Érudit Passionné"
            } else if (streak >= 3) {
                "Étudiant Assidu"
            } else {
                "Apprenti"
            }

            // Added logic for Completion Forecast
            val forecast = if (currentProgress >= 100) {
                "Objectif atteint ! Félicitations."
            } else if (currentProgress > 0) {
                val remaining = 100 - currentProgress
                if (streak > 0) {
                    "Encore $remaining% ! À ce rythme, bientôt fini."
                } else {
                    "Continuez vos efforts pour atteindre les 100%."
                }
            } else {
                "Prêt pour votre première leçon ?"
            }

            // Added logic for Learning Velocity
            val velocity = if (streak > 5 && score > 500) {
                "En pleine accélération 🚀"
            } else if (streak > 0) {
                "Progression constante"
            } else {
                "Reprise en douceur"
            }

            // Added logic for Seasonal Messages
            val month = calendar.get(java.util.Calendar.MONTH)
            val seasonal = if (month == java.util.Calendar.JANUARY) {
                "Bonne année ! Nouveau départ pour votre français."
            } else if (month == java.util.Calendar.DECEMBER) {
                "Joyeuses fêtes ! Prenez soin de vous."
            } else if (month >= 5 && month <= 7) {
                "L'été arrive ! Parfait pour étudier dehors."
            } else {
                null
            }

            // Added logic for Daily Goal
            val lessonsToday = 0 // Mocking for now, should come from DB
            val dailyGoal = 1.0f
            val goalProgress = if (lessonsToday >= dailyGoal) 1.0f else (lessonsToday / dailyGoal)

            // Added logic for Global Ranking
            val globalRank = if (score > 10000) {
                "Top 1%"
            } else if (score > 5000) {
                "Top 5%"
            } else if (score > 1000) {
                "Top 20%"
            } else if (score > 100) {
                "Top 50%"
            } else {
                "En attente"
            }

            // Added logic for XP Multiplier based on Streak
            val multiplier = if (streak >= 14) {
                2.0f
            } else if (streak >= 7) {
                1.5f
            } else if (streak >= 3) {
                1.2f
            } else {
                1.0f
            }

            // Added logic for Next Reward prediction
            val nextGoal = if (level < 8) {
                "Niveau ${level + 1}"
            } else if (score < 10000) {
                "Badge Légende"
            } else {
                "Expert Suprême"
            }

            // Added logic for Rating Prompt (If user is doing well)
            val shouldRate = if (currentMastered >= 5 && currentAccuracy >= 80 && streak >= 3) {
                true
            } else {
                false
            }

            // Added logic for Activity Recommendation based on time
            val activity = if (hour in 7..10) {
                "Dictionnaire"
            } else if (hour in 18..22) {
                "Quiz"
            } else {
                "Cours"
            }

            _uiState.update { it.copy(
                userName = name,
                streak = streak,
                score = score,
                level = level,
                isLoading = false,
                accuracy = currentAccuracy,
                encouragement = quote,
                greeting = timeGreeting,
                performanceTier = tier,
                statusColor = color,
                isWeekend = isWeekend,
                studyTip = tip,
                userTitle = title,
                completionForecast = forecast,
                learningVelocity = velocity,
                seasonalMessage = seasonal,
                dailyGoalProgress = goalProgress,
                ranking = globalRank,
                xpMultiplier = multiplier,
                nextReward = nextGoal,
                showRatingPrompt = shouldRate,
                recommendedActivity = activity
            )}
        }
    }

    fun updateStats(progress: Int, mastered: Int, reviews: Int) {
        // Recalculate forecast based on updated progress
        val forecast = if (progress >= 100) {
            "Objectif atteint ! Félicitations."
        } else if (progress > 0) {
            val remaining = 100 - progress
            "Encore $remaining% ! Continuez vos efforts."
        } else {
            "Prêt pour votre première leçon ?"
        }

        _uiState.update { it.copy(
            overallProgress = progress,
            masteredLessons = mastered,
            dueReviews = reviews,
            completionForecast = forecast
        )}
    }
}
