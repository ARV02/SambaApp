package com.example.samba.di

import android.content.Context
import androidx.room.Room
import com.example.samba.data.local.SambaDatabase
import com.example.samba.data.local.dao.ConnectionProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSambaDatabase(@ApplicationContext context: Context): SambaDatabase {
        return Room.databaseBuilder(
            context,
            SambaDatabase::class.java,
            "samba_database"
        ).build()
    }

    @Provides
    fun provideConnectionProfileDao(database: SambaDatabase): ConnectionProfileDao {
        return database.connectionProfileDao()
    }
}
