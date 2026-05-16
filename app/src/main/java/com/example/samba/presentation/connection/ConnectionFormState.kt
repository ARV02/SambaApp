package com.example.samba.presentation.connection

data class ConnectionFormState(
    val profileName: String = "",
    val host: String = "",
    val shareName: String = "",
    val username: String = "",
    val password: String = "",
    val selectedPreset: ConnectionPreset = ConnectionPreset.Custom,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val canSubmit: Boolean
        get() = profileName.isNotBlank()
                && host.isNotBlank()
                && shareName.isNotBlank()
                && username.isNotBlank()
                && password.isNotBlank()
}
