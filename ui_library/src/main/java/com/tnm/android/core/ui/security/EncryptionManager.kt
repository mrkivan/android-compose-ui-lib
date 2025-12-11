package com.tnm.android.core.ui.security

interface EncryptionManager {
    fun encrypt(plainText: String): String

    fun decrypt(encryptedData: String): String
}

class EncryptionException(message: String, cause: Throwable? = null) : Exception(message, cause)
class DecryptionException(message: String, cause: Throwable? = null) : Exception(message, cause)