package com.example.samba.presentation.connection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.samba.domain.usecase.profile.ConnectionProfileUseCases
import com.example.samba.domain.usecase.profile.ValidationResult
import com.example.samba.model.SmbConnectionProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class ConnectionFormViewModel @Inject constructor(
    private val connectionProfileUseCases: ConnectionProfileUseCases
) : ViewModel() {

    private val _uiState = MutableLiveData<ConnectionFormUiState>(ConnectionFormUiState.Idle)
    val uiState: LiveData<ConnectionFormUiState> = _uiState

    private val _formState = MutableLiveData(ConnectionFormState())
    val formState: LiveData<ConnectionFormState> = _formState

    fun onProfileNameChanged(value: String) {
        updateState { copy(profileName = value) }
    }

    fun onHostChanged(value: String) {
        updateState { copy(host = value) }
    }

    fun onShareNameChanged(value: String) {
        updateState { copy(shareName = value) }
    }

    fun onUsernameChanged(value: String) {
        updateState { copy(username = value) }
    }

    fun onPasswordChanged(value: String) {
        updateState { copy(password = value) }
    }

    fun onPresetSelected(preset: ConnectionPreset) {
        updateState {
            copy(
                selectedPreset = preset,
                profileName = preset.defaultProfileName.ifBlank { profileName },
                shareName = preset.defaultShareName.ifBlank { shareName }
            )
        }
    }

    fun submit() {
        val currentState = _formState.value ?: ConnectionFormState()

        val validationResult = connectionProfileUseCases.validateConnectionProfileUseCase(
            profileName = currentState.profileName,
            host = currentState.host,
            shareName = currentState.shareName,
            username = currentState.username
        )

        if (validationResult is ValidationResult.Invalid) {
            _uiState.value = ConnectionFormUiState.ValidationError(validationResult.message)
            return
        }

        if (currentState.password.isBlank()) {
            _uiState.value = ConnectionFormUiState.ValidationError("Password is required.")
            return
        }

        val connectionProfile = SmbConnectionProfile(
            name = currentState.profileName.trim(),
            host = currentState.host.trim(),
            shareName = currentState.shareName.trim(),
            username = currentState.username.trim()
        )

        _uiState.value = ConnectionFormUiState.Success(
            connectionProfile = connectionProfile,
            password = currentState.password,
            rememberPassword = currentState.rememberPassword
        )
    }

    fun clearError() {
        updateState { copy(errorMessage = null) }
    }

    private fun updateState(
        reducer: ConnectionFormState.() -> ConnectionFormState
    ) {
        _formState.value = (_formState.value ?: ConnectionFormState()).reducer()
    }

    fun resetForm() {
        _formState.value = ConnectionFormState()
        _uiState.value = ConnectionFormUiState.Idle
    }

    fun onRememberPasswordChanged(value: Boolean) {
        updateState {
            copy(rememberPassword = value)
        }
    }
}
