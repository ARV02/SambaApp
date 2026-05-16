package com.example.samba.presentation.filebrowser

import com.example.samba.domain.model.SmbFileItem

data class FileBrowserUiState(
    val isLoading: Boolean = false,
    val files: List<SmbFileItem> = emptyList(),
    val currentPath: String = "",
    val errorMessage: String? = null
) {
    val isEmpty: Boolean
        get() = !isLoading && errorMessage == null && files.isEmpty()
}
