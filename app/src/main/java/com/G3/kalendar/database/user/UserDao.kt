package com.G3.kalendar.database.user

import android.util.Log
import com.G3.kalendar.Globals
import com.G3.kalendar.database.user.User.Companion.toUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserDao(private val db: FirebaseFirestore) {

    private val TAG: String = UserDao::class.java.simpleName
    private val hashSaltUtil = HashSaltUtil()

    suspend fun insert(user: User) {
        val salt = hashSaltUtil.getSalt()
        val password = hashSaltUtil.getHashedPassword(user.password, salt)

        val entry = hashMapOf(
            Globals.NAME_FIELD to user.name,
            Globals.EMAIL_FIELD to user.email,
            Globals.PASSWORD_FIELD to password,
            Globals.SALT_FIELD to salt
        )

        db.collection(Globals.USER_TABLE_NAME)
            .add(entry)
            .await()
    }

    suspend fun authenticate(email: String, password: String): User? {
        val user = try {
            val query = db.collection(Globals.USER_TABLE_NAME)
                .whereEqualTo(Globals.EMAIL_FIELD, email)
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
            db.collection(Globals.USER_TABLE_NAME)
                .document(userId).get().await().toUser()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user details", e)
            null
        }
    }

    suspend fun getAll(): List<User> {
        return try {
            db.collection(Globals.USER_TABLE_NAME).get().await()
                .documents.mapNotNull { it.toUser() }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting all users", e)
            emptyList()
        }
    }

    suspend fun changePassword(userId: String, password: String) {
        val salt = hashSaltUtil.getSalt()
        val hashedPassword = hashSaltUtil.getHashedPassword(password, salt)

        db.collection(Globals.USER_TABLE_NAME).document(userId)
            .update(
                mapOf(
                    Globals.PASSWORD_FIELD to hashedPassword,
                    Globals.SALT_FIELD to salt
                )
            ).await()
    }
}