package com.G3.kalendar.database

import android.util.Log
import com.G3.kalendar.database.User.Companion.toUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserDao(private val db: FirebaseFirestore) {

    private val TAG: String = UserDao::class.java.simpleName
    private val hashSaltUtil = HashSaltUtil()

    suspend fun insert(user: User) {
        val salt = hashSaltUtil.getSalt()
        val password = hashSaltUtil.getHashedPassword(user.password, salt)

        val entry = hashMapOf(
            "name" to user.name,
            "email" to user.email,
            "password" to password,
            "salt" to salt
        )

        db.collection("users")
            .add(entry)
            .await()
    }

    suspend fun authenticate(email: String, password: String): User? {
        val user = try {
            val query = db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()

            // Assuming emails are unique so there should never be more than 1 match
            if (query.documents.size > 0) {
                query.documents[0].toUser()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user details", e)
            null
        }
        if (user != null) {
            val hashedPassword = hashSaltUtil.getHashedPassword(password, user.salt)
            if (user.password == hashedPassword) {
                return user
            } else {
                println("Debug: passwords dont match")
            }
        } else {
            println("Debug: user not found")
        }
        return null
    }

    suspend fun getUser(userId: String): User? {
        return try {
            db.collection("users")
                .document(userId).get().await().toUser()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user details", e)
            null
        }
    }

    suspend fun getAll(): List<User> {
        return try {
            db.collection("users").get().await()
                .documents.mapNotNull { it.toUser() }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting all users", e)
            emptyList()
        }
    }
}