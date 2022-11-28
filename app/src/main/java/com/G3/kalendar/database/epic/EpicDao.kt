package com.G3.kalendar.database.epic

import android.util.Log
import com.G3.kalendar.Globals
import com.G3.kalendar.database.epic.Epic.Companion.toEpic
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EpicDao(private val db: FirebaseFirestore) {

    private val TAG: String = EpicDao::class.java.simpleName

    suspend fun insert(epic: Epic) {
        val entry = hashMapOf(
            Globals.USER_ID_FIELD to epic.userId,
            Globals.TITLE_FIELD to epic.title
        )

        db.collection(Globals.EPIC_TABLE_NAME)
            .add(entry)
            .await()
    }

    suspend fun getAllByUserId(userId: String): List<Epic> {
        val epics = ArrayList<Epic>()
        try {
            val query = db.collection(Globals.EPIC_TABLE_NAME)
                .whereEqualTo(Globals.USER_ID_FIELD, userId)
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

    suspend fun delete(id: String) {
        db.collection(Globals.EPIC_TABLE_NAME).document(id).delete().await()
    }
}