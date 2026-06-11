package com.example.mongrammaire.courslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern

data class DetailsUiState(
    val steps: List<LearningStep> = emptyList(),
    val currentStepIndex: Int = 0,
    val progress: Int = 0,
    val isFinished: Boolean = false
)

data class LearningStep(
    val type: String,
    val content: String
)

class DetailsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    fun loadContent(rawText: String?) {
        if (rawText == null || rawText.isEmpty()) return

        viewModelScope.launch {
            val steps = parseContent(rawText)
            _uiState.value = _uiState.value.copy(
                steps = steps,
                currentStepIndex = 0,
                progress = if (steps.isNotEmpty()) 100 / steps.size else 0
            )
        }
    }

    private fun parseContent(rawText: String): List<LearningStep> {
        val steps = mutableListOf<LearningStep>()
        val pattern = Pattern.compile("\\[(RULE|EXAMPLE|EXCEPTION)]")
        val matcher = pattern.matcher(rawText)

        val foundTags = mutableListOf<String>()
        val startIndices = mutableListOf<Int>()

        while (matcher.find()) {
            foundTags.add(matcher.group(0))
            startIndices.add(matcher.start())
        }

        if (startIndices.isEmpty()) {
            steps.add(LearningStep("[RULE]", rawText))
            return steps
        }

        for (i in startIndices.indices) {
            val start = startIndices[i] + foundTags[i].length
            val end = if (i + 1 < startIndices.size) startIndices[i + 1] else rawText.length
            steps.add(LearningStep(foundTags[i], rawText.substring(start, end).trim()))
        }
        return steps
    }

    fun nextStep() {
        val state = _uiState.value
        if (state.currentStepIndex < state.steps.size - 1) {
            val nextIndex = state.currentStepIndex + 1
            _uiState.value = state.copy(
                currentStepIndex = nextIndex,
                progress = ((nextIndex + 1) * 100) / state.steps.size
            )
        } else {
            _uiState.value = state.copy(isFinished = true)
        }
    }
}
