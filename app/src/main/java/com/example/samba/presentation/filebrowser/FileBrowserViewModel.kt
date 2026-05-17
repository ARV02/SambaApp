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

    private var activeConnectionKey: String? = null

    fun initialize(
        connectionProfile: SmbConnectionProfile,
        password: String
    ) {
        val nextConnectionKey = buildConnectionKey(
            connectionProfile = connectionProfile,
            password = password
        )

        val shouldReload = activeConnectionKey != nextConnectionKey

        this.connectionProfile = connectionProfile
        this.password = password
        activeConnectionKey = nextConnectionKey

        if (shouldReload) {
            listFiles(path = "")
        }
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

    private fun buildConnectionKey(
        connectionProfile: SmbConnectionProfile,
        password: String
    ): String {
        return listOf(
            connectionProfile.name,
            connectionProfile.host,
            connectionProfile.shareName,
            connectionProfile.username,
            password.hashCode().toString()
        ).joinToString(separator = "|")
    }

    fun listFiles(
        path: String = currentPath(),
        actionMessage: FileBrowserActionMessage? = null
    ) {
        val profile = connectionProfile ?: return
        val currentPassword = password ?: return

        listFilesJob?.cancel()

        _uiState.value = _uiState.value?.copy(
            isLoading = true,
            errorMessage = null,
            actionMessage = null
        ) ?: FileBrowserUiState(
            isLoading = true,
            currentPath = path,
            actionMessage = null
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
                        errorMessage = null,
                        actionMessage = actionMessage
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

    fun createFolder(folderName: String) {
        val profile = connectionProfile ?: return
        val currentPassword = password ?: return
        val path = currentPath()

        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, errorMessage = null)

            when (
                val result = repository.createFolder(
                    connectionProfile = profile,
                    password = currentPassword,
                    currentPath = path,
                    folderName = folderName
                )
            ) {
                is SmbFileResult.Success -> {
                    listFiles(
                        path = path,
                        actionMessage = FileBrowserActionMessage(
                            "Folder created successfully.",
                            FileBrowserActionMessageType.Success
                        )
                    )
                }

                is SmbFileResult.Error -> {
                    _uiState.value = _uiState.value?.copy(
                        isLoading = false,
                        actionMessage = FileBrowserActionMessage(
                            result.message,
                            FileBrowserActionMessageType.Error
                        )
                    )
                }
            }
        }
    }

    fun createFile(fileName: String) {
        val profile = connectionProfile ?: return
        val currentPassword = password ?: return
        val path = currentPath()

        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, errorMessage = null)

            when (
                val result = repository.createFile(
                    connectionProfile = profile,
                    password = currentPassword,
                    currentPath = path,
                    fileName = fileName
                )
            ) {
                is SmbFileResult.Success -> {
                    listFiles(
                        path = path,
                        actionMessage = FileBrowserActionMessage(
                            "File created successfully.",
                            FileBrowserActionMessageType.Success
                        )
                    )
                }

                is SmbFileResult.Error -> {
                    _uiState.value = _uiState.value?.copy(
                        isLoading = false,
                        actionMessage = FileBrowserActionMessage(
                            result.message,
                            FileBrowserActionMessageType.Error
                        )
                    )
                }
            }
        }
    }

    fun deleteItem(item: SmbFileItem) {
        val profile = connectionProfile ?: return
        val currentPassword = password ?: return
        val path = currentPath()

        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true, errorMessage = null)

            val result = if (item.isDirectory) {
                repository.deleteEmptyFolder(
                    connectionProfile = profile,
                    password = currentPassword,
                    folderPath = item.path
                )
            } else {
                repository.deleteFile(
                    connectionProfile = profile,
                    password = currentPassword,
                    filePath = item.path
                )
            }

            when (result) {
                is SmbFileResult.Success -> {
                    val successMessage = if (item.isDirectory) {
                        "Folder deleted successfully."
                    } else {
                        "File deleted successfully."
                    }

                    listFiles(
                        path = path,
                        actionMessage = FileBrowserActionMessage(
                            successMessage,
                            FileBrowserActionMessageType.Success
                        )
                    )
                }

                is SmbFileResult.Error -> {
                    _uiState.value = _uiState.value?.copy(
                        isLoading = false,
                        actionMessage = FileBrowserActionMessage(
                            result.message,
                            FileBrowserActionMessageType.Error
                        )
                    )
                }
            }
        }
    }

    fun clearActionMessage() {
        _uiState.value = _uiState.value?.copy(
            actionMessage = null
        )
    }
}
