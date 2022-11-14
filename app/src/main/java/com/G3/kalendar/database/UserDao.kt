package com.G3.kalendar.database

import android.util.Log
import com.G3.kalendar.database.User.Companion.toUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserDao(private val db: FirebaseFirestore) {

    private val TAG: String = UserDao::class.java.simpleName

    suspend fun insert(user: User) {
        val entry = hashMapOf(
            "name" to user.name,
            "email" to user.email,
            "password" to user.password
        )

        db.collection("users")
            .add(entry)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
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