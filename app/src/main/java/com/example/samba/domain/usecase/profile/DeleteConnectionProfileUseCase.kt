package com.example.samba.domain.usecase.profile

import com.example.samba.domain.repository.ConnectionProfileRepository
import javax.inject.Inject

class DeleteConnectionProfileUseCase @Inject constructor(
    private val repository: ConnectionProfileRepository
) {

    suspend operator fun invoke(id: Long) {
        repository.deleteProfileById(id)
    }
}