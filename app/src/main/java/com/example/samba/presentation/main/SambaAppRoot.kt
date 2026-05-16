package com.example.samba.presentation.main

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.samba.presentation.connection.ConnectionRoute
import com.example.samba.presentation.filebrowser.FileBrowserRoute
import com.example.samba.presentation.profiles.ConnectionProfilesScreen
import com.example.samba.presentation.settings.SettingsScreen

@Composable
fun SambaAppRoot(
    viewModel: SambaAppRootViewModel = viewModel()
) {
    val currentScreen = viewModel.currentScreen

    BackHandler(
        enabled = currentScreen != MainScreen.Profiles
    ) {
        viewModel.handleBack()
    }

    when (currentScreen) {
        MainScreen.Profiles -> {
            ConnectionProfilesScreen(
                onNewConnectionClick = {
                    viewModel.openNewConnection()
                },
                onSettingsClick = {
                    viewModel.openSettings()
                }
            )
        }

        MainScreen.NewConnection -> {
            ConnectionRoute(
                onConnectionReady = { connectionProfile, password ->
                    viewModel.openFileBrowser(
                        connectionProfile = connectionProfile,
                        password = password
                    )
                },
                onBackClick = {
                    viewModel.openProfiles()
                }
            )
        }

        is MainScreen.FileBrowser -> {
            FileBrowserRoute(
                connectionProfile = currentScreen.connectionProfile,
                password = currentScreen.password,
                onBackClick = {
                    viewModel.openNewConnection()
                }
            )
        }

        MainScreen.Settings -> {
            SettingsScreen(
                onBackClick = {
                    viewModel.openNewConnection()
                }
            )
        }
    }
}