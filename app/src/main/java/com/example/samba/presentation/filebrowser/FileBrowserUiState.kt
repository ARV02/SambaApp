package com.example.samba.presentation.filebrowser

import com.example.samba.domain.model.SmbFileItem

sealed class FileBrowserUiState {

    object Idle : FileBrowserUiState()
    object Loading : FileBrowserUiState()
    data class Success(val files: List<SmbFileItem>) : FileBrowserUiState()
    data class Error(val message: String) : FileBrowserUiState()
}
