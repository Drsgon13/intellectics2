package proglife.com.ua.intellektiks.utils

import android.util.Base64
import java.io.UnsupportedEncodingException
import java.security.GeneralSecurityException
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


/**
 * Created by Evhenyi Shcherbyna on 23.01.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class AESCrypt {

    companion object {
        private const val AES_MODE = "AES/CBC/PKCS7Padding"
        private val CHARSET = Charsets.UTF_8
        private const val HASH_ALGORITHM = "SHA-256"
        private val ivBytes = byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
    }

    private fun generateKey(password: String): SecretKeySpec {
        val digest = MessageDigest.getInstance(HASH_ALGORITHM)
        val bytes = password.toByteArray(charset("UTF-8"))
        digest.update(bytes, 0, bytes.size)
        val key = digest.digest()
        return SecretKeySpec(key, "AES")
    }

    @Throws(GeneralSecurityException::class)
    fun encrypt(password: String, message: String): String {
        try {
            val key = generateKey(password)
            val cipherText = encrypt(key, ivBytes, message.toByteArray(CHARSET))
            return Base64.encodeToString(cipherText, Base64.NO_WRAP)
        } catch (e: UnsupportedEncodingException) {
            throw GeneralSecurityException(e)
        }
    }

    @Throws(GeneralSecurityException::class)
    private fun encrypt(key: SecretKeySpec, iv: ByteArray, message: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(AES_MODE)
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)
        return cipher.doFinal(message)
    }

    @Throws(GeneralSecurityException::class)
    fun decrypt(password: String, base64EncodedCipherText: String): String {
        try {
            val key = generateKey(password)
            val decodedCipherText = Base64.decode(base64EncodedCipherText, Base64.NO_WRAP)
            val decryptedBytes = decrypt(key, ivBytes, decodedCipherText)
            return String(decryptedBytes, CHARSET)
        } catch (e: UnsupportedEncodingException) {
            throw GeneralSecurityException(e)
        }
    }

    @Throws(GeneralSecurityException::class)
    private fun decrypt(key: SecretKeySpec, iv: ByteArray, decodedCipherText: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(AES_MODE)
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)
        return cipher.doFinal(decodedCipherText)
    }

}