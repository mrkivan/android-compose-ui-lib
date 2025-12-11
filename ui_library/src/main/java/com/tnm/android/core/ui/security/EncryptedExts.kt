package com.tnm.android.core.ui.security

import java.math.BigDecimal

@JvmInline
value class EncryptedString(val value: String) {
    companion object {
        private fun getCryptoManager() = EncryptionInitializer.getManager()

        fun encrypt(plain: String) = EncryptedString(getCryptoManager().encrypt(plain))
        fun decrypt(encrypted: EncryptedString) = getCryptoManager().decrypt(encrypted.value)
    }
}

@JvmInline
value class EncryptedBigDecimal(val value: String) {
    companion object {
        private fun getCryptoManager() = EncryptionInitializer.getManager()

        fun encrypt(amount: BigDecimal) = EncryptedBigDecimal(getCryptoManager().encrypt(amount.toPlainString()))
        fun decrypt(encrypted: EncryptedBigDecimal): BigDecimal {
            return BigDecimal(getCryptoManager().decrypt(encrypted.value))
        }
    }
}