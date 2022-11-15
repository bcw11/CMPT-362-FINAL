package com.G3.kalendar.database.user

import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var salt: String = ""
) : Parcelable {
    companion object {
        fun DocumentSnapshot.toUser(): User? {
            return try {
                val name = getString("name")!!
                val email = getString("email")!!
                val password = getString("password")!!
                val salt = getString("salt")!!
                User(id, name, email, password, salt)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting user profile", e)
                null
            }
        }

        private val TAG: String = User::class.java.simpleName
    }
}