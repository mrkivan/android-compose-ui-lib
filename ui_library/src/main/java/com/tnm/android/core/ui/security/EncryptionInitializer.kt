package com.tnm.android.core.ui.security

object EncryptionInitializer {
    private lateinit var encryptionManager: EncryptionManager

    fun initialize(manager: EncryptionManager) {
        this.encryptionManager = manager
    }

    fun getManager(): EncryptionManager {
        if (!::encryptionManager.isInitialized) {
            error("EncryptionManager has not been initialized. Call EncryptionInitializer.initialize() in your Application class.")
        }
        return encryptionManager
    }
}