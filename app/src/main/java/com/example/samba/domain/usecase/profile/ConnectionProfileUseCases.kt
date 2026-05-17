package com.example.samba.domain.usecase.profile

import javax.inject.Inject

data class ConnectionProfileUseCases @Inject constructor(
    val observeProfiles: ObserveConnectionProfilesUseCase,
    val saveProfile: SaveConnectionProfileUseCase,
    val updateProfile: UpdateConnectionProfileUseCase,
    val deleteProfile: DeleteConnectionProfileUseCase,
    val updateLastConnectedAt: UpdateLastConnectedAtUseCase
)