package com.G3.kalendar.database.epic

import android.util.Log
import com.G3.kalendar.database.epic.Epic.Companion.toEpic
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EpicDao(private val db: FirebaseFirestore) {

    private val TAG: String = EpicDao::class.java.simpleName
    private val TABLE_NAME = "epics"

    suspend fun insert(epic: Epic) {
        val entry = hashMapOf(
            "userId" to epic.userId,
            "title" to epic.title
        )

        db.collection(TABLE_NAME)
            .add(entry)
            .await()
    }

    suspend fun getAllByUserId(userId: String): List<Epic> {
        val epics = ArrayList<Epic>()
        try {
            val query = db.collection(TABLE_NAME)
                .whereEqualTo("userId", userId)
                .get()
                .await()

            for (document in query.documents) {
                document.toEpic()?.let { epics.add(it) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting matching stories", e)
        }
        return epics
    }
}