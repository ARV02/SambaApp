package com.example.samba.presentation.filebrowser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samba.data.smb.SmbFileRepositoryImpl
import com.example.samba.domain.model.SmbFileResult
import com.example.samba.domain.repository.SmbFileRepository
import com.example.samba.model.SmbConnectionProfile
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FileBrowserViewModel (
    private val repository: SmbFileRepository = SmbFileRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableLiveData<FileBrowserUiState>(FileBrowserUiState.Idle)
    val uiState: LiveData<FileBrowserUiState> = _uiState

    private var listFilesJob: Job? = null

    fun listFiles(
        connectionProfile: SmbConnectionProfile,
        password: String
    ) {
        listFilesJob?.cancel()
        _uiState.value = FileBrowserUiState.Loading

        listFilesJob = viewModelScope.launch {
            when (val result = repository.listFiles(connectionProfile, password)) {
                is SmbFileResult.Success -> {
                    _uiState.value = FileBrowserUiState.Success(result.data)
                }
                is SmbFileResult.Error -> {
                    _uiState.value = FileBrowserUiState.Error(result.message)
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
