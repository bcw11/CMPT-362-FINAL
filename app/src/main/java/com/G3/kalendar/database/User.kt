package com.G3.kalendar.database

import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var password: String = ""
) : Parcelable {
    companion object {
        fun DocumentSnapshot.toUser(): User? {
            return try {
                val name = getString("name")!!
                val email = getString("email")!!
                val password = getString("password")!!
                User(id, name, email, password)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting user profile", e)
                null
            }
        }

        private const val TAG = "User"
    }
}