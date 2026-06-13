package com.example.mongrammaire.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * 2. Reactive Architecture & Immutable State
 */
data class SettingsUiState(
    val isDarkMode: Boolean = false,
    val isMetric: Boolean = true,
    val isMasterUnitsEnabled: Boolean = true,
    val isLoading: Boolean = false
)

sealed class SettingsUiEvent {
    data class ShowToast(val message: String) : SettingsUiEvent()
}

class SettingsViewModel(private val repository: ISettingsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SettingsUiEvent>()
    val events: SharedFlow<SettingsUiEvent> = _events.asSharedFlow()

    init {
        observeRepository()
    }

    private fun observeRepository() {
        viewModelScope.launch {
            combine(
                repository.getTheme(),
                repository.getUnitSystem(),
                repository.getMasterUnitSwitch()
            ) { dark, metric, master ->
                SettingsUiState(
                    isDarkMode = dark,
                    isMetric = metric,
                    isMasterUnitsEnabled = master,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    /**
     * 3. Interactivity Guarding & Error Rollovers
     */
    fun onThemeToggled(enabled: Boolean) {
        val previousState = _uiState.value.isDarkMode
        // Optimistic UI Update
        _uiState.update { it.copy(isDarkMode = enabled) }
        
        viewModelScope.launch {
            repository.saveTheme(enabled).onFailure {
                // Rollback on failure
                _uiState.update { it.copy(isDarkMode = previousState) }
                _events.emit(SettingsUiEvent.ShowToast("Erreur lors de la sauvegarde du thème"))
            }
        }
    }

    fun onUnitsChanged(metric: Boolean) {
        val previousState = _uiState.value.isMetric
        _uiState.update { it.copy(isMetric = metric) }

        viewModelScope.launch {
            repository.saveUnitSystem(metric).onFailure {
                _uiState.update { it.copy(isMetric = previousState) }
                _events.emit(SettingsUiEvent.ShowToast("Erreur lors de la sauvegarde du système d'unités"))
            }
        }
    }

    fun onMasterUnitsToggled(enabled: Boolean) {
        val previousState = _uiState.value.isMasterUnitsEnabled
        _uiState.update { it.copy(isMasterUnitsEnabled = enabled) }

        viewModelScope.launch {
            repository.saveMasterUnitSwitch(enabled).onFailure {
                _uiState.update { it.copy(isMasterUnitsEnabled = previousState) }
                _events.emit(SettingsUiEvent.ShowToast("Erreur lors du changement de configuration"))
            }
        }
    }
}
