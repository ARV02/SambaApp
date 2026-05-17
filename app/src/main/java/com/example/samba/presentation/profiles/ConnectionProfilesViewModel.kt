package com.example.samba.presentation.profiles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samba.domain.usecase.profile.ConnectionProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectionProfilesViewModel @Inject constructor(
    private val profileUseCases: ConnectionProfileUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConnectionProfilesUiState())
    val uiState: StateFlow<ConnectionProfilesUiState> = _uiState.asStateFlow()

    init {
        observeProfiles()
    }

    private fun observeProfiles() {
        viewModelScope.launch {
            profileUseCases.observeProfiles().collect { profiles ->
                _uiState.value = ConnectionProfilesUiState(
                    isLoading = false,
                    profiles = profiles,
                    errorMessage = null
                )
            }
        }
    }

    fun deleteProfile(id: Long) {
        viewModelScope.launch {
            profileUseCases.deleteProfile(id)
        }
    }

    fun updateLastConnectedAt(id: Long) {
        viewModelScope.launch {
            profileUseCases.updateLastConnectedAt(id)
        }
    }

    fun saveProfile(
        profileName: String,
        host: String,
        shareName: String,
        username: String
    ) {
        viewModelScope.launch {
            profileUseCases.saveProfile(
                profileName = profileName,
                host = host,
                shareName = shareName,
                username = username
            )
        }
    }
}
