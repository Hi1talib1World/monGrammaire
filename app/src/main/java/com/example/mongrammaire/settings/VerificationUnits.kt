package com.example.mongrammaire.settings

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * 4. Fake Repository for Unit Verification (Simulation of I/O)
 */
class FakeSettingsRepository(
    private val shouldFail: Boolean = false,
    private val ioDelay: Long = 1000
) : ISettingsRepository {

    private val themeFlow = MutableStateFlow(false)
    private val unitFlow = MutableStateFlow(true)
    private val masterFlow = MutableStateFlow(true)

    override fun saveTheme(isDark: Boolean): Result<Unit> {
        if (shouldFail) return Result.failure(Exception("IO Error"))
        themeFlow.value = isDark
        return Result.success(Unit)
    }

    override fun getTheme(): Flow<Boolean> = themeFlow

    override fun saveUnitSystem(metric: Boolean): Result<Unit> {
        if (shouldFail) return Result.failure(Exception("Disk Full"))
        unitFlow.value = metric
        return Result.success(Unit)
    }

    override fun getUnitSystem(): Flow<Boolean> = unitFlow

    override fun saveMasterUnitSwitch(enabled: Boolean): Result<Unit> {
        masterFlow.value = enabled
        return Result.success(Unit)
    }

    override fun getMasterUnitSwitch(): Flow<Boolean> = masterFlow
}

/**
 * OUTLINE FOR JUNIT TESTS
 * Use with MockK/JUnit 5 or 4
 */
/*
class SettingsViewModelTest {
    
    @Test
    fun `initial state mirrors repository truth`() = runTest {
        val repo = FakeSettingsRepository()
        val viewModel = SettingsViewModel(repo)
        
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(false, state.isDarkMode)
            assertEquals(true, state.isMetric)
        }
    }

    @Test
    fun `triggering toggle event modifies state instantly`() = runTest {
        val repo = FakeSettingsRepository()
        val viewModel = SettingsViewModel(repo)
        
        viewModel.onThemeToggled(true)
        assertEquals(true, viewModel.uiState.value.isDarkMode)
    }

    @Test
    fun `error during save triggers rollback`() = runTest {
        val repo = FakeSettingsRepository(shouldFail = true)
        val viewModel = SettingsViewModel(repo)
        
        val initialState = viewModel.uiState.value.isDarkMode
        viewModel.onThemeToggled(!initialState)
        
        // Advance time for co-routine if needed
        assertEquals(initialState, viewModel.uiState.value.isDarkMode)
    }
}
*/
