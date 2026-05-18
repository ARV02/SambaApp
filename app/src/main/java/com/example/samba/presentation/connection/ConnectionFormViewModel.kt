package com.example.samba.presentation.connection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samba.domain.model.SmbFileResult
import com.example.samba.domain.repository.SmbFileRepository
import com.example.samba.domain.usecase.profile.ConnectionProfileUseCases
import com.example.samba.domain.usecase.profile.ValidationResult
import com.example.samba.model.SmbConnectionProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ConnectionFormViewModel @Inject constructor(
    private val connectionProfileUseCases: ConnectionProfileUseCases,
    private val smbFileRepository: SmbFileRepository
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
        val currentState = formState.value ?: ConnectionFormState()

        val validationResult = connectionProfileUseCases.validateConnectionProfileUseCase(
            profileName = currentState.profileName,
            host = currentState.host,
            shareName = currentState.shareName,
            username = currentState.username
        )

        if (validationResult is ValidationResult.Invalid) {
            _uiState.value = ConnectionFormUiState.ValidationError(
                message = validationResult.message
            )
            return
        }

        if (currentState.password.isBlank()) {
            _uiState.value = ConnectionFormUiState.ValidationError(
                message = "Password is required."
            )
            return
        }

        val connectionProfile = SmbConnectionProfile(
            name = currentState.profileName.trim(),
            host = currentState.host.trim(),
            shareName = currentState.shareName.trim(),
            username = currentState.username.trim()
        )

        _formState.value = currentState.copy(isLoading = true)
        _uiState.value = ConnectionFormUiState.Idle

        viewModelScope.launch {
            when (
                val result = smbFileRepository.listFiles(
                    connectionProfile = connectionProfile,
                    password = currentState.password,
                    path = ""
                )
            ) {
                is SmbFileResult.Success -> {
                    _formState.value = currentState.copy(isLoading = false)

                    _uiState.value = ConnectionFormUiState.Success(
                        connectionProfile = connectionProfile,
                        password = currentState.password,
                        rememberPassword = currentState.rememberPassword
                    )
                }

                is SmbFileResult.Error -> {
                    _formState.value = currentState.copy(isLoading = false)

                    _uiState.value = ConnectionFormUiState.ValidationError(
                        message = result.message
                    )
                }
            }
        }
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
