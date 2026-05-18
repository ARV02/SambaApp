package com.example.samba.di

import com.example.samba.data.local.repository.ConnectionProfileRepositoryImpl
import com.example.samba.data.settings.AppSettingsRepositoryImpl
import com.example.samba.data.smb.SmbFileRepositoryImpl
import com.example.samba.domain.repository.AppSettingsRepository
import com.example.samba.domain.repository.ConnectionProfileRepository
import com.example.samba.domain.repository.SmbFileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindConnectionProfileRepository(
        impl: ConnectionProfileRepositoryImpl
    ): ConnectionProfileRepository

    @Binds
    @Singleton
    abstract fun bindSmbFileRepository(
        impl: SmbFileRepositoryImpl
    ): SmbFileRepository

    @Binds
    @Singleton
    abstract fun bindAppSettingsRepository(
        impl: AppSettingsRepositoryImpl
    ): AppSettingsRepository
}
