package com.example.samba.data.local.repository

import com.example.samba.data.local.dao.ConnectionProfileDao
import com.example.samba.data.local.entity.ConnectionProfileEntity
import com.example.samba.data.local.mapper.toDomain
import com.example.samba.data.local.mapper.toEntity
import com.example.samba.domain.model.ConnectionProfile
import com.example.samba.domain.repository.ConnectionProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ConnectionProfileRepositoryImpl @Inject constructor(
    private val dao: ConnectionProfileDao
): ConnectionProfileRepository {

    override fun observeProfiles(): Flow<List<ConnectionProfile>> {
        return dao.observeProfiles().map { profiles ->
            profiles.map { it.toDomain() }
        }
    }

    override suspend fun getProfileById(id: Long): ConnectionProfile? {
        return dao.getProfileById(id)?.toDomain()
    }

    override suspend fun saveProfile(
        profileName: String,
        host: String,
        shareName: String,
        username: String
    ): Long {
        val cleanProfileName = profileName.trim()
        val cleanHost = host.trim()
        val cleanShareName = shareName.trim()
        val cleanUsername = username.trim()
        val now = System.currentTimeMillis()

        val existingProfile = dao.findProfile(
            host = cleanHost,
            shareName = cleanShareName,
            username = cleanUsername
        )

        return if (existingProfile != null) {
            dao.updateProfile(
                existingProfile.copy(
                    profileName = cleanProfileName,
                    updatedAt = now,
                    lastConnectedAt = now
                )
            )
            existingProfile.id
        } else {
            dao.insertProfile(
                ConnectionProfileEntity(
                    profileName = cleanProfileName,
                    host = cleanHost,
                    shareName = cleanShareName,
                    username = cleanUsername,
                    createdAt = now,
                    updatedAt = now,
                    lastConnectedAt = now
                )
            )
        }
    }

    override suspend fun updateProfile(profile: ConnectionProfile) {
        dao.updateProfile(
            profile.copy(
                updatedAt = System.currentTimeMillis()
            ).toEntity()
        )
    }

    override suspend fun deleteProfile(profile: ConnectionProfile) {
        dao.deleteProfile(profile.toEntity())
    }

    override suspend fun deleteProfileById(id: Long) {
        dao.deleteProfileById(id)
    }

    override suspend fun updateLastConnectedAt(id: Long) {
        dao.updateLastConnectedAt(
            id = id,
            lastConnectedAt = System.currentTimeMillis()
        )
    }
}
