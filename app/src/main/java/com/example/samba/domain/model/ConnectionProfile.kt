package com.example.samba.domain.model

data class ConnectionProfile(
    val id: Long,
    val profileName: String,
    val host: String,
    val shareName: String,
    val username: String,
    val createdAt: Long,
    val updatedAt: Long,
    val lastConnectedAt: Long?
)