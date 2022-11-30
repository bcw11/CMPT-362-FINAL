package com.G3.kalendar.database.story

import android.util.Log
import com.G3.kalendar.Globals
import com.G3.kalendar.database.story.Story.Companion.toStory
import com.G3.kalendar.database.task.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class StoryDao(private val db: FirebaseFirestore) {

    private val TAG: String = StoryDao::class.java.simpleName

    suspend fun insert(story: Story) {
        val entry = hashMapOf(
            Globals.USER_ID_FIELD to story.userId,
            Globals.EPIC_ID_FIELD to story.epicId,
            Globals.NAME_FIELD to story.name,
            Globals.DUE_DATE_FIELD to story.dueDate,
            Globals.STATUS_FIELD to story.status,
            Globals.CALENDAR_TIMES_FIELD to story.calendarTimes,
            Globals.COLOR_FIELD to story.color
        )

        db.collection(Globals.STORY_TABLE_NAME)
            .add(entry)
            .await()
    }

    suspend fun insertWithTasks(story: Story, taskList: ArrayList<Task>) {
        val storyEntry = hashMapOf(
            Globals.USER_ID_FIELD to story.userId,
            Globals.EPIC_ID_FIELD to story.epicId,
            Globals.NAME_FIELD to story.name,
            Globals.DUE_DATE_FIELD to story.dueDate,
            Globals.STATUS_FIELD to story.status,
            Globals.CALENDAR_TIMES_FIELD to story.calendarTimes,
            Globals.COLOR_FIELD to story.color
        )

        db.collection(Globals.STORY_TABLE_NAME)
            .add(storyEntry)
            .addOnSuccessListener {
                for (task in taskList) {
                    task.storyId = it.id
                    val taskEntry = hashMapOf(
                        Globals.USER_ID_FIELD to task.userId,
                        Globals.STORY_ID_FIELD to task.storyId,
                        Globals.NAME_FIELD to task.name,
                        Globals.STATUS_FIELD to task.status
                    )
                    db.collection(Globals.TASK_TABLE_NAME)
                        .add(taskEntry)
                }
            }
            .await()
    }

    suspend fun getAllByUserId(userId: String): List<Story> {
        val stories = ArrayList<Story>()
        try {
            val query = db.collection(Globals.STORY_TABLE_NAME)
                .whereEqualTo(Globals.USER_ID_FIELD, userId)
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
                .whereEqualTo(Globals.USER_ID_FIELD, userId)
                .whereEqualTo(Globals.EPIC_ID_FIELD, epicId)
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

    suspend fun delete(story: Story) {
        db.collection(Globals.STORY_TABLE_NAME).document(story.id).delete().await()
    }

    suspend fun update(story: Story) {
        db.collection(Globals.STORY_TABLE_NAME).document(story.id)
            .update(
                mapOf(
                    Globals.EPIC_ID_FIELD to story.epicId,
                    Globals.NAME_FIELD to story.name,
                    Globals.DUE_DATE_FIELD to story.dueDate,
                    Globals.STATUS_FIELD to story.status,
                    Globals.CALENDAR_TIMES_FIELD to story.calendarTimes
                )
            ).await()
    }
}