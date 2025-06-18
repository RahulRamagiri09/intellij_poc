package com.example.sidebarplugin.utils

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionUtils {
    fun encryptPassword(password: String): String {
        return try {
            val key = "1234567890123456".toByteArray(Charsets.UTF_8) // 16-char key
            val iv = "abcdefghijklmnop".toByteArray(Charsets.UTF_8)   // 16-char IV

            val keySpec = SecretKeySpec(key, "AES")
            val ivSpec = IvParameterSpec(iv)

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)

            val encrypted = cipher.doFinal(password.toByteArray(Charsets.UTF_8))
            Base64.getEncoder().encodeToString(encrypted)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}
