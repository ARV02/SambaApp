package com.example.samba.data.security

import android.content.Context
import android.util.Base64
import com.example.samba.domain.repository.CredentialStorageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class SecureCredentialStorageRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val keystoreManager: AndroidKeystoreManager
) : CredentialStorageRepository {

    private val preferences = context.getSharedPreferences(
        "secure_credentials",
        Context.MODE_PRIVATE
    )

    override fun savePassword(profileId: Long, password: String) {
        val cipher = keystoreManager.createEncryptCipher()
        val encryptedBytes = cipher.doFinal(password.toByteArray(Charsets.UTF_8))

        val encryptedPassword = Base64.encodeToString(
            encryptedBytes,
            Base64.NO_WRAP
        )

        val iv = Base64.encodeToString(
            cipher.iv,
            Base64.NO_WRAP
        )

        preferences.edit {
            putString(passwordKey(profileId), encryptedPassword)
                .putString(ivKey(profileId), iv)
        }
    }

    override fun getPassword(profileId: Long): String? {
        val encryptedPassword = preferences.getString(passwordKey(profileId), null)
            ?: return null

        val iv = preferences.getString(ivKey(profileId), null)
            ?: return null

        val encryptedBytes = Base64.decode(encryptedPassword, Base64.NO_WRAP)
        val ivBytes = Base64.decode(iv, Base64.NO_WRAP)

        val cipher = keystoreManager.createDecryptCipher(ivBytes)
        val decryptedBytes = cipher.doFinal(encryptedBytes)

        return decryptedBytes.toString(Charsets.UTF_8)
    }

    override fun deletePassword(profileId: Long) {
        preferences.edit {
            remove(passwordKey(profileId))
                .remove(ivKey(profileId))
        }
    }

    override fun hasPassword(profileId: Long): Boolean {
        return preferences.contains(passwordKey(profileId)) &&
                preferences.contains(ivKey(profileId))
    }

    private fun passwordKey(profileId: Long): String {
        return "profile_${profileId}_password"
    }

    private fun ivKey(profileId: Long): String {
        return "profile_${profileId}_iv"
    }
}
