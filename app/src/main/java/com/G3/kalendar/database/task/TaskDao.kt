package com.G3.kalendar.database.task

import android.util.Log
import com.G3.kalendar.Globals
import com.G3.kalendar.database.task.Task.Companion.toTask
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TaskDao(private val db: FirebaseFirestore) {

    private val TAG: String = TaskDao::class.java.simpleName

    suspend fun insert(task: Task) {
        val entry = hashMapOf(
            Globals.USER_ID_FIELD to task.userId,
            Globals.STORY_ID_FIELD to task.storyId,
            Globals.NAME_FIELD to task.name,
            Globals.STATUS_FIELD to task.status
        )

        db.collection(Globals.TASK_TABLE_NAME)
            .add(entry)
            .await()
    }

    suspend fun getAllByUserId(userId: String): List<Task> {
        val tasks = ArrayList<Task>()
        try {
            val query = db.collection(Globals.TASK_TABLE_NAME)
                .whereEqualTo(Globals.USER_ID_FIELD, userId)
                .get()
                .await()

            for (document in query.documents) {
                document.toTask()?.let { tasks.add(it) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting matching stories", e)
        }
        return tasks
    }

    suspend fun getAllByStoryId(userId: String, storyId: String): List<Task> {
        val tasks = ArrayList<Task>()
        try {
            val query = db.collection(Globals.TASK_TABLE_NAME)
                .whereEqualTo(Globals.USER_ID_FIELD, userId)
                .whereEqualTo(Globals.STORY_ID_FIELD, storyId)
                .get()
                .await()

            for (document in query.documents) {
                document.toTask()?.let { tasks.add(it) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting matching stories", e)
        }
        return tasks
    }

    suspend fun updateTaskStatus(taskId: String, status: Boolean) {
        db.collection(Globals.TASK_TABLE_NAME).document(taskId)
            .update(
                mapOf(
                    Globals.STATUS_FIELD to status
                )
            ).await()
    }
}