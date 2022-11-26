package com.G3.kalendar.database.task

import android.os.Parcelable
import android.util.Log
import com.G3.kalendar.Globals
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    var id: String = "",
    var userId: String = "",
    var storyId: String = "",
    var name: String = "",
    var status: Boolean = false
) : Parcelable {
    companion object {
        fun DocumentSnapshot.toTask(): Task? {
            return try {
                val userId = getString(Globals.USER_ID_FIELD)!!
                val storyId = getString(Globals.STORY_ID_FIELD)!!
                val name = getString(Globals.NAME_FIELD)!!
                val status = getBoolean(Globals.STATUS_FIELD)!!
                Task(id, userId, storyId, name, status)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting story entry", e)
                null
            }
        }

        private val TAG: String = Task::class.java.simpleName
    }
}