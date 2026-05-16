package com.example.samba.presentation.filebrowser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samba.data.smb.SmbFileRepositoryImpl
import com.example.samba.domain.model.SmbFileItem
import com.example.samba.domain.model.SmbFileResult
import com.example.samba.domain.repository.SmbFileRepository
import com.example.samba.model.SmbConnectionProfile
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FileBrowserViewModel (
    private val repository: SmbFileRepository = SmbFileRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableLiveData(FileBrowserUiState())
    val uiState: LiveData<FileBrowserUiState> = _uiState

    private var connectionProfile: SmbConnectionProfile? = null
    private var password: String? = null

    private var listFilesJob: Job? = null

    fun initialize(
        connectionProfile: SmbConnectionProfile,
        password: String
    ) {
        this.connectionProfile = connectionProfile
        this.password = password
        listFiles(path = "")
    }

    fun openFolder(folder: SmbFileItem) {
        if (!folder.isDirectory) return
        listFiles(folder.path)
    }

    fun goBackFolder(): Boolean {
        val path = currentPath()

        if (path.isBlank()) {
            return false
        }

        val parentPath = path.substringBeforeLast("/", missingDelimiterValue = "")

        listFiles(parentPath)
        return true
    }

    fun refresh() {
        listFiles(currentPath())
    }

    private fun currentPath(): String {
        return _uiState.value?.currentPath.orEmpty()
    }

    fun listFiles(path: String = currentPath()) {
        val profile = connectionProfile ?: return
        val currentPassword = password ?: return

        listFilesJob?.cancel()

        _uiState.value = _uiState.value?.copy(
            isLoading = true,
            errorMessage = null
        ) ?: FileBrowserUiState(
            isLoading = true,
            currentPath = path
        )

        listFilesJob = viewModelScope.launch {
            when (
                val result = repository.listFiles(
                    connectionProfile = profile,
                    password = currentPassword,
                    path = path
                )
            ) {
                is SmbFileResult.Success -> {
                    _uiState.value = FileBrowserUiState(
                        isLoading = false,
                        files = result.data,
                        currentPath = path,
                        errorMessage = null
                    )
                }

                is SmbFileResult.Error -> {
                    _uiState.value = _uiState.value?.copy(
                        isLoading = false,
                        files = emptyList(),
                        currentPath = path,
                        errorMessage = result.message
                    ) ?: FileBrowserUiState(
                        isLoading = false,
                        files = emptyList(),
                        currentPath = path,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun cancelCurrentOperation() {
        listFilesJob?.cancel()
        listFilesJob = null
    }

    override fun onCleared() {
        cancelCurrentOperation()
        super.onCleared()
    }
}
