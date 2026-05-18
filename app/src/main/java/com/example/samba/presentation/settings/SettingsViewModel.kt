package com.example.samba.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samba.domain.repository.AppSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appsSettingsRepository: AppSettingsRepository
) : ViewModel() {

    val readOnlyMode: StateFlow<Boolean> =
        appsSettingsRepository.readOnlyMode.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun onReadOnlyModeChanged(enabled: Boolean) {
        viewModelScope.launch {
            appsSettingsRepository.setReadOnly(enabled)
        }
    }

}
