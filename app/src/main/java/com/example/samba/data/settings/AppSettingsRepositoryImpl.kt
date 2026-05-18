package com.example.samba.data.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.samba.domain.repository.AppSettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.appSettingsDataStore by preferencesDataStore(
    name = "app_settings"
)

@Singleton
class AppSettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): AppSettingsRepository {

    private val readOnlyModeKey = booleanPreferencesKey("read_only_mode")

    override val readOnlyMode: Flow<Boolean> =
        context.appSettingsDataStore.data.map { preferences ->
            preferences[readOnlyModeKey] ?: false
        }

    override suspend fun setReadOnly(enable: Boolean) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[readOnlyModeKey] = enable
        }
    }
}
