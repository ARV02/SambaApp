package com.example.samba.presentation.connection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.samba.model.SmbConnectionProfile

class ConnectionFormViewModel : ViewModel() {

    private val _uiState = MutableLiveData<ConnectionFormUiState>(ConnectionFormUiState.Idle)
    val uiState: LiveData<ConnectionFormUiState> = _uiState

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
}
