package com.example.samba.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.samba.data.local.dao.ConnectionProfileDao
import com.example.samba.data.local.entity.ConnectionProfileEntity

@Database(
    entities = [
        ConnectionProfileEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class SambaDatabase : RoomDatabase() {

    abstract fun connectionProfileDao(): ConnectionProfileDao
}
