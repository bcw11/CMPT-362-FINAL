package com.G3.kalendar.database

import android.util.Log
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom

class HashSaltUtil() {
    private val TAG: String = HashSaltUtil::class.java.simpleName

    fun getHashedPassword(password: String, salt: String): String? {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            md.update(salt.toByteArray())
            BigInteger(1, md.digest(password.toByteArray())).toString(16).padStart(32, '0')
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