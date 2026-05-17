package com.example.samba.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "connection_profiles")
data class ConnectionProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val profileName: String,
    val host: String,
    val shareName: String,
    val username: String,
    val createdAt: Long,
    val updatedAt: Long,
    val lastConnectedAt: Long? = null
)