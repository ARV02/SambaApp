package com.example.samba.domain.repository

import com.example.samba.domain.model.SmbFileItem
import com.example.samba.domain.model.SmbFileResult
import com.example.samba.model.SmbConnectionProfile

interface SmbFileRepository {

    suspend fun listFiles(
        connectionProfile: SmbConnectionProfile,
        password: String
    ): SmbFileResult<List<SmbFileItem>>
}
