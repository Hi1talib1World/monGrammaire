package com.example.mongrammaire.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mongrammaire.Utils.ToastHelper
import com.example.mongrammaire.databinding.ActivitySettingsBinding
import com.example.mongrammaire.notifications.NotificationHelper
import kotlinx.coroutines.launch

/**
 * 2. Activity Observing and Immediate Side-Effects
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    
    // Manual injection as requested (using a simple factory)
    private val viewModel: SettingsViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(SettingsRepositoryImpl(applicationContext)) as T
            }
        })[SettingsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            NotificationHelper.setNotificationsEnabled(isChecked)
            if (isChecked) {
                NotificationHelper.scheduleDailyNotification(this)
                ToastHelper.showCustomToast(this, "Notifications activées")
            } else {
                NotificationHelper.cancelNotification(this)
                ToastHelper.showCustomToast(this, "Notifications désactivées")
            }
        }

        binding.switchEnableUnits.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onMasterUnitsToggled(isChecked)
        }

        binding.rgUnitSystem.setOnCheckedChangeListener { _, checkedId ->
            viewModel.onUnitsChanged(checkedId == binding.rbMetric.id)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        updateUi(state)
                    }
                }
                launch {
                    viewModel.events.collect { event ->
                        when (event) {
                            is SettingsUiEvent.ShowToast -> ToastHelper.showCustomToast(this@SettingsActivity, event.message)
                        }
                    }
                }
            }
        }
    }

    private fun updateUi(state: SettingsUiState) {
        // Interactivity Guarding (Point 3)
        binding.switchNotifications.isChecked = NotificationHelper.isNotificationsEnabled()
        binding.switchEnableUnits.isChecked = state.isMasterUnitsEnabled
        
        binding.rgUnitSystem.isEnabled = state.isMasterUnitsEnabled
        binding.rbMetric.isEnabled = state.isMasterUnitsEnabled
        binding.rbImperial.isEnabled = state.isMasterUnitsEnabled
        
        if (state.isMetric) {
            binding.rbMetric.isChecked = true
        } else {
            binding.rbImperial.isChecked = true
        }
    }
}
