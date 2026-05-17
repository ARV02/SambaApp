package com.example.samba.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.samba.data.local.entity.ConnectionProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConnectionProfileDao {

    @Query("SELECT * FROM connection_profiles ORDER BY updatedAt DESC")
    fun observeProfiles(): Flow<List<ConnectionProfileEntity>>

    @Query("SELECT * FROM connection_profiles WHERE id = :id LIMIT 1")
    suspend fun getProfileById(id: Long): ConnectionProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ConnectionProfileEntity): Long

    @Update
    suspend fun updateProfile(profile: ConnectionProfileEntity)

    @Delete
    suspend fun deleteProfile(profile: ConnectionProfileEntity)

    @Query("DELETE FROM connection_profiles WHERE id = :id")
    suspend fun deleteProfileById(id: Long)

    @Query("UPDATE connection_profiles SET lastConnectedAt = :lastConnectedAt WHERE id = :id")
    suspend fun updateLastConnectedAt(
        id: Long,
        lastConnectedAt: Long
    )

    @Query("""
    SELECT * FROM connection_profiles 
    WHERE host = :host 
    AND shareName = :shareName 
    AND username = :username 
    LIMIT 1
""")
    suspend fun findProfile(
        host: String,
        shareName: String,
        username: String
    ): ConnectionProfileEntity?
}
