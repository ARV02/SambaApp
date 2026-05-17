package com.example.samba.data.local.mapper

import com.example.samba.data.local.entity.ConnectionProfileEntity
import com.example.samba.domain.model.ConnectionProfile

fun ConnectionProfileEntity.toDomain(): ConnectionProfile {
    return ConnectionProfile(
        id = id,
        profileName = profileName,
        host = host,
        shareName = shareName,
        username = username,
        createdAt = createdAt,
        updatedAt = updatedAt,
        lastConnectedAt = lastConnectedAt
    )
}

fun ConnectionProfile.toEntity(): ConnectionProfileEntity {
    return ConnectionProfileEntity(
        id = id,
        profileName = profileName,
        host = host,
        shareName = shareName,
        username = username,
        createdAt = createdAt,
        updatedAt = updatedAt,
        lastConnectedAt = lastConnectedAt
    )
}