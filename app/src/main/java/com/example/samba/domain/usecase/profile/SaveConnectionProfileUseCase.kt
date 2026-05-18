package com.example.samba.domain.usecase.profile

import com.example.samba.domain.repository.ConnectionProfileRepository
import javax.inject.Inject

class SaveConnectionProfileUseCase @Inject constructor(
    private val repository: ConnectionProfileRepository
) {

    suspend operator fun invoke(
        profileName: String,
        host: String,
        shareName: String,
        username: String
    ): Long {
        return repository.saveProfile(
            profileName = profileName.trim(),
            host = host.trim(),
            shareName = shareName.trim(),
            username = username.trim()
        )
    }
}