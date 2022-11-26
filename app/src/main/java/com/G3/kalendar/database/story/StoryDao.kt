package com.G3.kalendar.database.story

import android.util.Log
import com.G3.kalendar.Globals
import com.G3.kalendar.database.story.Story.Companion.toStory
import com.G3.kalendar.database.user.UserDao
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class StoryDao(private val db: FirebaseFirestore) {

    private val TAG: String = StoryDao::class.java.simpleName

    suspend fun insert(story: Story) {
        val entry = hashMapOf(
            "userId" to story.userId,
            "epicId" to story.epicId,
            "name" to story.name,
            "dueDate" to story.dueDate,
            "status" to story.status,
            "calendarTimes" to story.calendarTimes
        )

        db.collection(Globals.STORY_TABLE_NAME)
            .add(entry)
            .await()
    }

    suspend fun getAllByUserId(userId: String): List<Story> {
        val stories = ArrayList<Story>()
        try {
            val query = db.collection(Globals.STORY_TABLE_NAME)
                .whereEqualTo("userId", userId)
                .get()
                .await()

            for (document in query.documents) {
                document.toStory()?.let { stories.add(it) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting matching stories", e)
        }
        return stories
    }

    suspend fun getAllByEpicId(userId: String, epicId: String): List<Story> {
        val stories = ArrayList<Story>()
        try {
            val query = db.collection(Globals.STORY_TABLE_NAME)
                .whereEqualTo("userId", userId)
                .whereEqualTo("epicId", epicId)
                .get()
                .await()

            for (document in query.documents) {
                document.toStory()?.let { stories.add(it) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting matching stories", e)
        }
        return stories
    }
}