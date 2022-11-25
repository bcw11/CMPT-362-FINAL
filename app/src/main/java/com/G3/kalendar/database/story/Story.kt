package com.G3.kalendar.database.story

import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    var id: String = "",
    var userId: String = "",
    var epicId: String = "",
    var name: String = "",
    var dueDate: Long = 0L,
    var status: String = "",
    var calendarTimes: List<Long> = ArrayList()
) : Parcelable {
    companion object {
        fun DocumentSnapshot.toStory(): Story? {
            return try {
                val userId = getString("userId")!!
                val epicId = getString("epicId")!!
                val name = getString("name")!!
                val dueDate = getLong("dueDate")!!
                val status = getString("status")!!
                val calendarTimes = data!!["calendarTimes"]!! as List<Long>

                Story(id, userId, epicId, name, dueDate, status, calendarTimes)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting story entry", e)
                null
            }
        }

        private val TAG: String = Story::class.java.simpleName
    }
}