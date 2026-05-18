package com.example.samba.domain.repository

import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {

    val readOnlyMode: Flow<Boolean>

    suspend fun setReadOnly(enable: Boolean)
}
