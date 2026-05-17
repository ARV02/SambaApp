package com.example.samba.domain.repository

import com.example.samba.domain.model.SmbFileItem
import com.example.samba.domain.model.SmbFileResult
import com.example.samba.model.SmbConnectionProfile

interface SmbFileRepository {

    suspend fun listFiles(
        connectionProfile: SmbConnectionProfile,
        password: String,
        path: String = ""
    ): SmbFileResult<List<SmbFileItem>>

    suspend fun createFolder(
        connectionProfile: SmbConnectionProfile,
        password: String,
        currentPath: String,
        folderName: String
    ): SmbFileResult<Unit>

    suspend fun createFile(
        connectionProfile: SmbConnectionProfile,
        password: String,
        currentPath: String,
        fileName: String
    ): SmbFileResult<Unit>

    suspend fun deleteFile(
        connectionProfile: SmbConnectionProfile,
        password: String,
        filePath: String
    ): SmbFileResult<Unit>

    suspend fun deleteEmptyFolder(
        connectionProfile: SmbConnectionProfile,
        password: String,
        folderPath: String
    ): SmbFileResult<Unit>
}
