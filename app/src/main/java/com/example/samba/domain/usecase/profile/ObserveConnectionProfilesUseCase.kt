package com.example.samba.domain.usecase.profile

import com.example.samba.domain.model.ConnectionProfile
import com.example.samba.domain.repository.ConnectionProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveConnectionProfilesUseCase @Inject constructor(
    private val repository: ConnectionProfileRepository
) {

    operator fun invoke(): Flow<List<ConnectionProfile>> {
        return repository.observeProfiles()
    }
}