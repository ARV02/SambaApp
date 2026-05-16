package com.example.samba.presentation.connection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.samba.model.SmbConnectionProfile

class ConnectionFormViewModel : ViewModel() {

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
        val state = _formState.value ?: ConnectionFormState()
        validateAndCreateProfile(
            profileName = state.profileName,
            host = state.host,
            shareName = state.shareName,
            username = state.username,
            password = state.password
        )
    }

    fun validateAndCreateProfile(
        profileName: String,
        host: String,
        shareName: String,
        username: String,
        password: String
    ) {
        val cleanProfileName = profileName.trim()
        val cleanHost = host.trim()
        val cleanShareName = shareName.trim()
        val cleanUsername = username.trim()

        if (cleanProfileName.isBlank()
            || cleanHost.isBlank()
            || cleanShareName.isBlank()
            || cleanUsername.isBlank()
            || password.isBlank()
        ) {

            _uiState.value = ConnectionFormUiState.ValidationError(
                "All fields are required."
            )
            return
        }

        val connectionProfile = SmbConnectionProfile(
            name = cleanProfileName,
            host = cleanHost,
            shareName = cleanShareName,
            username = cleanUsername
        )

        _uiState.value = ConnectionFormUiState.Success(
            connectionProfile = connectionProfile,
            password = password
        )
    }

    fun resetState() {
        _uiState.value = ConnectionFormUiState.Idle
    }

    fun clearError() {
        updateState { copy(errorMessage = null) }
    }

    private fun updateState(
        reducer: ConnectionFormState.() -> ConnectionFormState
    ) {
        _formState.value = (_formState.value ?: ConnectionFormState()).reducer()
    }
}
