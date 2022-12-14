package com.G3.kalendar.database.epic

import android.os.Parcelable
import android.util.Log
import com.G3.kalendar.Globals
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class Epic(
    var id: String = "",
    var userId: String = "",
    var title: String = ""
) : Parcelable {
    companion object {
        fun DocumentSnapshot.toEpic(): Epic? {
            return try {
                val userId = getString(Globals.USER_ID_FIELD)!!
                val title = getString(Globals.TITLE_FIELD)!!
                Epic(id, userId, title)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting story entry", e)
                null
            }
        }

        private val TAG: String = Epic::class.java.simpleName
    }
}