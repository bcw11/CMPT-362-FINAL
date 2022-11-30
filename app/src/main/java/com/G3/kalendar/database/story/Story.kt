package com.G3.kalendar.database.story

import android.os.Parcelable
import android.util.Log
import com.G3.kalendar.Globals
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize
import java.lang.reflect.Array.getInt

@Parcelize
data class Story(
    var id: String = "",
    var userId: String = "",
    var epicId: String = "",
    var name: String = "",
    var dueDate: Long = 0L,
    var status: String = "",
    var calendarTimes: List<Long> = ArrayList(),
    var color: Int = 0
) : Parcelable {
    companion object {
        fun DocumentSnapshot.toStory(): Story? {
            return try {
                val userId = getString(Globals.USER_ID_FIELD)!!
                val epicId = getString(Globals.EPIC_ID_FIELD)!!
                val name = getString(Globals.NAME_FIELD)!!
                val dueDate = getLong(Globals.DUE_DATE_FIELD)!!
                val status = getString(Globals.STATUS_FIELD)!!
                val calendarTimes = data!![Globals.CALENDAR_TIMES_FIELD]!! as List<Long>
                val color = getLong(Globals.COLOR_FIELD)!!.toInt()

                Story(id, userId, epicId, name, dueDate, status, calendarTimes, color)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting story entry", e)
                null
            }
        }

        private val TAG: String = Story::class.java.simpleName
    }
}