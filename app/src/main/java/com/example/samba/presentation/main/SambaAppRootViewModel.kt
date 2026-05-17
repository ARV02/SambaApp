package com.example.samba.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.samba.model.SmbConnectionProfile

class SambaAppRootViewModel : ViewModel() {

    var currentScreen by mutableStateOf<MainScreen>(MainScreen.Profiles)
        private set

    fun openProfiles() {
        currentScreen = MainScreen.Profiles
    }

    fun openNewConnection() {
        currentScreen = MainScreen.NewConnection
    }

    fun openSettings() {
        currentScreen = MainScreen.Settings
    }

    fun openFileBrowser(
        connectionProfile: SmbConnectionProfile,
        password: String,
        origin: FileBrowserOrigin
    ) {
        currentScreen = MainScreen.FileBrowser(
            connectionProfile = connectionProfile,
            password = password,
            origin
        )
    }

    fun handleBack() {
        currentScreen = when (val screen = currentScreen) {
            MainScreen.Profiles -> MainScreen.Profiles
            MainScreen.NewConnection -> MainScreen.Profiles
            MainScreen.Settings -> MainScreen.Profiles
            is MainScreen.FileBrowser -> {
                when (screen.origin) {
                    FileBrowserOrigin.NewConnection -> MainScreen.NewConnection
                    FileBrowserOrigin.SavedProfile -> MainScreen.Profiles
                }
            }
        }
    }

    fun handleFileBrowserBack(origin: FileBrowserOrigin) {
        currentScreen = when (origin) {
            FileBrowserOrigin.NewConnection -> MainScreen.NewConnection
            FileBrowserOrigin.SavedProfile -> MainScreen.Profiles
        }
    }
}
