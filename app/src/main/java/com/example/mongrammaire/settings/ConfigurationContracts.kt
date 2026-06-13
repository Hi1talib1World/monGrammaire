package com.example.mongrammaire.settings

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

/**
 * 1. Interface-Driven Architecture (The Contract)
 * Defines the strict operations for configuration persistence.
 */
interface ISettingsRepository {
    fun saveTheme(isDark: Boolean): Result<Unit>
    fun getTheme(): Flow<Boolean>
    fun saveUnitSystem(metric: Boolean): Result<Unit>
    fun getUnitSystem(): Flow<Boolean>
    fun saveMasterUnitSwitch(enabled: Boolean): Result<Unit>
    fun getMasterUnitSwitch(): Flow<Boolean>
}

/**
 * Implementation using SharedPreferences with immediate commits.
 */
class SettingsRepositoryImpl(context: Context) : ISettingsRepository {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_config", Context.MODE_PRIVATE)

    override fun saveTheme(isDark: Boolean): Result<Unit> = runCatching {
        if (!prefs.edit().putBoolean(KEY_DARK_MODE, isDark).commit()) {
            throw IllegalStateException("Failed to commit theme to disk")
        }
    }

    override fun getTheme(): Flow<Boolean> = prefs.booleanFlow(KEY_DARK_MODE, false)

    override fun saveUnitSystem(metric: Boolean): Result<Unit> = runCatching {
        if (!prefs.edit().putBoolean(KEY_METRIC, metric).commit()) {
            throw IllegalStateException("Failed to commit unit system to disk")
        }
    }

    override fun getUnitSystem(): Flow<Boolean> = prefs.booleanFlow(KEY_METRIC, true)

    override fun saveMasterUnitSwitch(enabled: Boolean): Result<Unit> = runCatching {
        if (!prefs.edit().putBoolean(KEY_MASTER_UNITS, enabled).commit()) {
            throw IllegalStateException("Failed to commit master switch to disk")
        }
    }

    override fun getMasterUnitSwitch(): Flow<Boolean> = prefs.booleanFlow(KEY_MASTER_UNITS, true)

    companion object {
        const val KEY_DARK_MODE = "pref_dark_mode"
        const val KEY_METRIC = "pref_metric_system"
        const val KEY_MASTER_UNITS = "pref_master_units_enabled"
    }
}

/**
 * Reactive extension to bridge SharedPreferences to Flow.
 */
fun SharedPreferences.booleanFlow(key: String, defaultValue: Boolean): Flow<Boolean> = callbackFlow {
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, k ->
        if (key == k) {
            trySend(sharedPreferences.getBoolean(key, defaultValue))
        }
    }
    registerOnSharedPreferenceChangeListener(listener)
    awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
}.onStart { emit(getBoolean(key, defaultValue)) }
