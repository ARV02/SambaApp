package com.example.samba.presentation.main

import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.samba.domain.model.ConnectionProfile
import com.example.samba.model.SmbConnectionProfile
import com.example.samba.presentation.components.SambaTextField
import com.example.samba.presentation.connection.ConnectionRoute
import com.example.samba.presentation.filebrowser.FileBrowserRoute
import com.example.samba.presentation.profiles.ConnectionProfilesScreen
import com.example.samba.presentation.profiles.ConnectionProfilesViewModel
import com.example.samba.presentation.settings.SettingsScreen

@Composable
fun SambaAppRoot(
    viewModel: SambaAppRootViewModel = viewModel(),
) {
    val profilesViewModel: ConnectionProfilesViewModel = hiltViewModel()
    val currentScreen = viewModel.currentScreen
    var selectedProfileForPassword by remember {
        mutableStateOf<ConnectionProfile?>(null)
    }

    BackHandler(
        enabled = currentScreen != MainScreen.Profiles
    ) {
        viewModel.handleBack()
    }

    when (currentScreen) {
        MainScreen.Profiles -> {
            val profilesViewModel: ConnectionProfilesViewModel = hiltViewModel()
            val profilesState by profilesViewModel.uiState.collectAsState()

            ConnectionProfilesScreen(
                state = profilesState,
                onNewConnectionClick = {
                    viewModel.openNewConnection()
                },
                onSettingsClick = {
                    viewModel.openSettings()
                },
                onProfileClick = { profile ->
                    selectedProfileForPassword = profile
                },
                onDeleteProfileClick = { profile ->
                    profilesViewModel.deleteProfile(profile.id)
                }
            )
        }

        MainScreen.NewConnection -> {
            ConnectionRoute(
                onConnectionReady = { connectionProfile, password ->
                    profilesViewModel.saveProfile(
                        profileName = connectionProfile.name,
                        host = connectionProfile.host,
                        shareName = connectionProfile.shareName,
                        username = connectionProfile.username
                    )

                    viewModel.openFileBrowser(
                        connectionProfile = connectionProfile,
                        password = password,
                        origin = FileBrowserOrigin.NewConnection
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
                    viewModel.handleFileBrowserBack(currentScreen.origin)
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

    selectedProfileForPassword?.let { profile ->
        SavedProfilePasswordDialog(
            profileName = profile.profileName,
            onDismiss = {
                selectedProfileForPassword = null
            },
            onConfirm = { password ->
                selectedProfileForPassword = null

                viewModel.openFileBrowser(
                    connectionProfile = profile.toSmbConnectionProfile(),
                    password = password,
                    origin = FileBrowserOrigin.SavedProfile
                )

                profilesViewModel.updateLastConnectedAt(profile.id)
            }
        )
    }
}

private fun ConnectionProfile.toSmbConnectionProfile(): SmbConnectionProfile {
    return SmbConnectionProfile(
        name = profileName,
        host = host,
        shareName = shareName,
        username = username
    )
}

@Composable
private fun SavedProfilePasswordDialog(
    profileName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var password by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF10172A),
        title = {
            Text(
                text = "Connect to $profileName",
                color = Color(0xFFF8FAFC),
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            SambaTextField(
                value = password,
                label = "Password",
                onValueChange = {
                    password = it
                },
                visualTransformation = PasswordVisualTransformation()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val cleanPassword = password.trim()

                    if (cleanPassword.isNotBlank()) {
                        onConfirm(cleanPassword)
                    }
                }
            ) {
                Text(
                    text = "Connect",
                    color = Color(0xFF818CF8)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Cancel",
                    color = Color(0xFF94A3B8)
                )
            }
        }
    )
}