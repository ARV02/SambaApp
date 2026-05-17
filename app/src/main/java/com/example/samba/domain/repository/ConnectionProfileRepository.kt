package com.example.samba.domain.repository

import com.example.samba.domain.model.ConnectionProfile
import kotlinx.coroutines.flow.Flow

interface ConnectionProfileRepository {

    fun observeProfiles(): Flow<List<ConnectionProfile>>

    suspend fun getProfileById(id: Long): ConnectionProfile?

    suspend fun saveProfile(
        profileName: String,
        host: String,
        shareName: String,
        username: String
    )

    suspend fun updateProfile(profile: ConnectionProfile)

    suspend fun deleteProfile(profile: ConnectionProfile)

    suspend fun deleteProfileById(id: Long)

    suspend fun updateLastConnectedAt(id: Long)
}
