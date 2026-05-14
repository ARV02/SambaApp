package com.example.samba.presentation.connection

import com.example.samba.model.SmbConnectionProfile

sealed class ConnectionFormUiState {

    object Idle : ConnectionFormUiState()
    data class Success(
        val connectionProfile: SmbConnectionProfile,
        val password: String
    ) : ConnectionFormUiState()
    data class ValidationError(val message: String) : ConnectionFormUiState()
}
