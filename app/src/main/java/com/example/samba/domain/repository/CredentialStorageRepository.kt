package com.example.samba.domain.repository

interface CredentialStorageRepository {

    fun savePassword(
        profileId: Long,
        password: String
    )

    fun getPassword(profileId: Long): String?

    fun deletePassword(profileId: Long)

    fun hasPassword(profileId: Long): Boolean
}
