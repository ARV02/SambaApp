package com.example.samba.di

import com.example.samba.data.security.AndroidKeystoreManager
import com.example.samba.data.security.SecureCredentialStorageRepositoryImpl
import com.example.samba.domain.repository.CredentialStorageRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecurityProviderModule {

    @Provides
    @Singleton
    fun provideAndroidKeystoreManager(): AndroidKeystoreManager {
        return AndroidKeystoreManager()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SecurityRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCredentialStorageRepository(
        impl: SecureCredentialStorageRepositoryImpl
    ): CredentialStorageRepository

}
