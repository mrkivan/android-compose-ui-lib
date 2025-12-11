package com.tnm.android.core.ui.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.UnrecoverableEntryException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class AndroidKeyStoreCryptoManager(
    private val keyAlias: String
) : EncryptionManager {

    private companion object {
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val GCM_IV_SIZE = 12
        private const val GCM_TAG_LENGTH = 128
        private const val CIPHER_ALGORITHM = "AES/GCM/NoPadding"
    }

    private val secretKey: SecretKey by lazy { getOrCreateSecretKey() }

    @Throws(
        KeyStoreException::class, NoSuchAlgorithmException::class, IOException::class,
        UnrecoverableEntryException::class, InvalidAlgorithmParameterException::class
    )
    private fun getOrCreateSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }

        if (keyStore.containsAlias(keyAlias)) {
            return (keyStore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry).secretKey
        }

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
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

    override fun encrypt(plainText: String): String {
        return try {
            val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)

            val iv = cipher.iv
            val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
            val combined = iv + encryptedBytes
            Base64.encodeToString(combined, Base64.DEFAULT)
        } catch (e: Exception) {
            throw EncryptionException("Failed to encrypt data using Android KeyStore.", e)
        }
    }

    override fun decrypt(encryptedData: String): String {
        if (encryptedData.isEmpty()) throw DecryptionException("Encrypted data cannot be empty.")

        return try {
            val combined = Base64.decode(encryptedData, Base64.DEFAULT)

            val iv = combined.copyOfRange(0, GCM_IV_SIZE)
            val encryptedBytes = combined.copyOfRange(GCM_IV_SIZE, combined.size)

            val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
            val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)

            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

            val decryptedBytes = cipher.doFinal(encryptedBytes)
            String(decryptedBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            throw DecryptionException("Failed to decrypt data using Android KeyStore.", e)
        }
    }
}