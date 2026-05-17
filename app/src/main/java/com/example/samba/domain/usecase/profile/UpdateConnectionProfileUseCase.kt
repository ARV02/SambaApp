package com.example.samba.domain.usecase.profile

import com.example.samba.domain.model.ConnectionProfile
import com.example.samba.domain.repository.ConnectionProfileRepository
import javax.inject.Inject

class UpdateConnectionProfileUseCase @Inject constructor(
    private val repository: ConnectionProfileRepository
) {

    suspend operator fun invoke(profile: ConnectionProfile) {
        repository.updateProfile(profile)
    }
}