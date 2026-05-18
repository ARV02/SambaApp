package com.example.samba.data.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class AndroidKeystoreManager {

    private val keyAlias = "samba_credentials_key"
    private val androidKeyStore = "AndroidKeyStore"
    private val transformation = "AES/GCM/NoPadding"

    fun getOrCreateSecretKey(): SecretKey {
        val keystore = KeyStore.getInstance(androidKeyStore).apply {
            load(null)
        }

        val existingKey = keystore.getKey(keyAlias, null) as? SecretKey

        if (existingKey != null) return existingKey

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            androidKeyStore
        )

        val keySpec = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()

        keyGenerator.init(keySpec)

        return keyGenerator.generateKey()
    }

    fun createEncryptCipher(): Cipher {
        val cipher = Cipher.getInstance(transformation)
        cipher.init(
            Cipher.ENCRYPT_MODE,
            getOrCreateSecretKey()
        )
        return cipher
    }

    fun createDecryptCipher(iv: ByteArray): Cipher {
        val cipher = Cipher.getInstance(transformation)
        cipher.init(
            Cipher.DECRYPT_MODE,
            getOrCreateSecretKey(),
            GCMParameterSpec(128, iv)
        )
        return cipher
    }
}