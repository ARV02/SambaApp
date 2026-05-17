package com.example.samba.presentation.profiles

import com.example.samba.domain.model.ConnectionProfile

data class ConnectionProfilesUiState(
    val isLoading: Boolean = true,
    val profiles: List<ConnectionProfile> = emptyList(),
    val errorMessage: String? = null
) {
    val isEmpty: Boolean
        get() = !isLoading && errorMessage == null && profiles.isEmpty()
}