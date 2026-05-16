package com.example.samba.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.samba.presentation.connection.ConnectionRoute
import com.example.samba.presentation.filebrowser.FileBrowserRoute
import com.example.samba.presentation.profiles.ConnectionProfilesScreen
import com.example.samba.presentation.settings.SettingsScreen

@Composable
fun SambaAppRoot() {
    var currentScreen by remember { mutableStateOf<MainScreen>(MainScreen.Profiles) }

    when (val screen = currentScreen) {
        MainScreen.Profiles -> {
            ConnectionProfilesScreen(
                onNewConnectionClick = {
                    currentScreen = MainScreen.NewConnection
                },
                onSettingsClick = {
                    currentScreen = MainScreen.Settings
                }
            )
        }

        MainScreen.NewConnection -> {
            ConnectionRoute(
                onConnectionReady = { connectionProfile, password ->
                    currentScreen = MainScreen.FileBrowser(
                        connectionProfile = connectionProfile,
                        password = password
                    )
                },
                onBackClick = {
                    currentScreen = MainScreen.Profiles
                }
            )
        }

        is MainScreen.FileBrowser -> {
            FileBrowserRoute(
                connectionProfile = screen.connectionProfile,
                password = screen.password,
                onBackClick = {
                    currentScreen = MainScreen.NewConnection
                }
            )
        }

        MainScreen.Settings -> {
            SettingsScreen(
                onBackClick = {
                    currentScreen = MainScreen.Profiles
                }
            )
        }
    }
}