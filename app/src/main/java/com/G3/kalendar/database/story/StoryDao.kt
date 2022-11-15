package com.G3.kalendar.database.story

import android.util.Log
import com.G3.kalendar.database.story.Story.Companion.toStory
import com.G3.kalendar.database.user.UserDao
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class StoryDao(private val db: FirebaseFirestore) {

    private val TAG: String = UserDao::class.java.simpleName

    suspend fun insert(story: Story) {
        val entry = hashMapOf(
            "userId" to story.userId,
            "title" to story.title,
            "description" to story.description,
            "timeStart" to story.timeStart,
            "timeEnd" to story.timeEnd
        )

        db.collection("stories")
            .add(entry)
            .await()
    }

    suspend fun getAllById(userId: String): List<Story> {
        val stories = ArrayList<Story>()
        try {
            val query = db.collection("stories")
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

    suspend fun getAll(): List<Story> {
        return try {
            db.collection("stories").get().await()
                .documents.mapNotNull { it.toStory() }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting all users", e)
            emptyList()
        }
    }
}