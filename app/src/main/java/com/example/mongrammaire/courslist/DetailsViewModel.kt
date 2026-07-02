package com.example.mongrammaire.courslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mongrammaire.Data.ILessonRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.regex.Pattern

/**
 * Pillar 3: Reactive UI State
 */
data class DetailsUiState(
    val lessonId: Int = -1,
    val steps: List<LearningStep> = emptyList(),
    val currentStepIndex: Int = 0,
    val progress: Int = 0,
    val isFinished: Boolean = false,
    val isSaving: Boolean = false, // Pillar 3: Component interlocking
    val errorEvent: String? = null,
    val isAutoNext: Boolean = false,
    val isMastered: Boolean = false
)

data class LearningStep(
    val type: String,
    val content: String,
    val revealContent: String? = null
)

class DetailsViewModel(private val repository: ILessonRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    fun initLesson(lessonId: Int, rawText: String?) {
        if (lessonId == -1 || rawText.isNullOrEmpty()) return

        viewModelScope.launch {
            val steps = parseContent(rawText)
            
            // Pillar 1: Load from persistent cache
            repository.getLessonProgress(lessonId).collect { savedIndex ->
                _uiState.update { it.copy(
                    lessonId = lessonId,
                    steps = steps,
                    currentStepIndex = savedIndex,
                    progress = calculateProgress(savedIndex, steps.size)
                )}
            }
            repository.saveSetting("last_lesson_id", lessonId.toString())
        }
    }

    private fun calculateProgress(index: Int, size: Int): Int {
        if (size == 0) return 0
        return ((index + 1) * 100) / size
    }

    private fun parseContent(rawText: String): List<LearningStep> {
        val steps = mutableListOf<LearningStep>()
        val pattern = Pattern.compile("\\[(RULE|EXAMPLE|EXCEPTION)]")
        val matcher = pattern.matcher(rawText)

        val foundTags = mutableListOf<String>()
        val startIndices = mutableListOf<Int>()

        while (matcher.find()) {
            foundTags.add(matcher.group(0)!!)
            startIndices.add(matcher.start())
        }

        if (startIndices.isEmpty()) {
            steps.add(LearningStep("[RULE]", rawText))
        } else {
            for (i in startIndices.indices) {
                val start = startIndices[i] + foundTags[i].length
                val end = if (i + 1 < startIndices.size) startIndices[i + 1] else rawText.length
                
                var content = rawText.substring(start, end).trim()
                var reveal: String? = null
                
                if (foundTags[i] == "[EXAMPLE]" && content.contains("-> [")) {
                    val rStart = content.indexOf("-> [")
                    val rEnd = content.indexOf("]", rStart)
                    if (rEnd != -1) {
                        reveal = content.substring(rStart + 4, rEnd)
                        content = content.substring(0, rStart).trim()
                    }
                }
                steps.add(LearningStep(foundTags[i], content, reveal))
            }
        }
        
        // Final completion step
        steps.add(LearningStep("[FINISH]", "Félicitations ! Vous avez terminé cette leçon."))
        return steps
    }

    /**
     * Pillar 2 & 4: Atomic Mutation with Safeguards
     */
    fun nextStep() {
        val state = _uiState.value
        if (state.isSaving || state.steps.isEmpty()) return

        val nextIndex = state.currentStepIndex + 1
        if (nextIndex >= state.steps.size) {
            _uiState.update { it.copy(isFinished = true) }
            return
        }

        viewModelScope.launch {
            // Pillar 3: Locking interactions
            _uiState.update { it.copy(isSaving = true) }

            // Simulate cloud sync or secondary side-effect (Pillar 2)
            delay(500)

            // Pillar 2 & 4: Atomic Persist and Rollback logic
            repository.saveLessonProgress(state.lessonId, nextIndex, nextIndex == state.steps.size - 1)
                .onSuccess {
                    _uiState.update { it.copy(
                        currentStepIndex = nextIndex,
                        progress = calculateProgress(nextIndex, it.steps.size),
                        isSaving = false
                    )}
                }
                .onFailure {
                    // Pillar 4: State Rollback & Notification
                    _uiState.update { it.copy(
                        isSaving = false,
                        errorEvent = "Échec de synchronisation. Veuillez réessayer."
                    )}
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorEvent = null) }
    }

    fun updateStepIndex(index: Int) {
        val state = _uiState.value
        if (index == state.currentStepIndex || index >= state.steps.size) return
        
        _uiState.update { it.copy(
            currentStepIndex = index,
            progress = calculateProgress(index, it.steps.size)
        )}
        
        viewModelScope.launch {
            repository.saveLessonProgress(state.lessonId, index, index == state.steps.size - 1)
        }
    }

    fun toggleAutoNext() {
        viewModelScope.launch {
            val newState = !_uiState.value.isAutoNext
            repository.saveSetting("auto_next", newState.toString())
            _uiState.update { it.copy(isAutoNext = newState) }
        }
    }

    fun toggleMastered() {
        viewModelScope.launch {
            val newState = !_uiState.value.isMastered
            repository.setLessonMastered(_uiState.value.lessonId, newState)
            _uiState.update { it.copy(isMastered = newState) }
        }
    }

    fun bookmarkCard(cardIndex: Int) {
        viewModelScope.launch {
            repository.setCardBookmarked(_uiState.value.lessonId, cardIndex)
        }
    }
}
