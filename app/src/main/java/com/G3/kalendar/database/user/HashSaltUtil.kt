package com.G3.kalendar.database.user

import android.util.Log
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom

class HashSaltUtil() {
    private val TAG: String = HashSaltUtil::class.java.simpleName

    // adapted from: https://stackoverflow.com/questions/64171624/how-to-generate-an-md5-hash-in-kotlin
    fun getHashedPassword(password: String, salt: String): String? {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            md.update(salt.toByteArray())
            val bytes = md.digest(password.toByteArray())
            BigInteger(1, bytes).toString(16).padStart(32, '0')
        } catch (e: NoSuchAlgorithmException) {
            Log.d(TAG, "Failed to find hash algorithm", e)
            null
        }
    }

    fun getSalt(): String {
        val secureRandom = SecureRandom.getInstance("SHA1PRNG")
        val salt = ByteArray(16)
        secureRandom.nextBytes(salt)
        return salt.toString()
    }
}