package com.G3.kalendar.database.story

import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize


@Parcelize
data class Story(
    var id: String = "",
    var userId: String = "",
    var title: String = "",
    var description: String = "",
    var timeStart: Long = 0L,
    var timeEnd: Long = 0L
) : Parcelable {
    companion object {
        fun DocumentSnapshot.toStory(): Story? {
            return try {
                val userId = getString("userId")!!
                val title = getString("title")!!
                val description = getString("description")!!
                val timeStart = getLong("timeStart")!!
                val timeEnd = getLong("timeEnd")!!
                Story(id, userId, title, description, timeStart, timeEnd)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting story entry", e)
                null
            }
        }

        private val TAG: String = Story::class.java.simpleName
    }
}