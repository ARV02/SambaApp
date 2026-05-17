package com.example.samba.presentation.main

import com.example.samba.model.SmbConnectionProfile

sealed class MainScreen {
    object Profiles : MainScreen()
    object NewConnection : MainScreen()
    object Settings : MainScreen()
    data class FileBrowser(
        val connectionProfile: SmbConnectionProfile,
        val password: String,
        val origin: FileBrowserOrigin
    ) : MainScreen()
}

enum class FileBrowserOrigin {
    NewConnection,
    SavedProfile
}
