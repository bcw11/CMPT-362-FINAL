package com.G3.kalendar.database.task

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskRepository(private val dao: TaskDao) {

    fun insert(task: Task) {
        CoroutineScope(IO).launch {
            dao.insert(task)
        }
    }

    suspend fun getAllByUserId(userId: String): List<Task> {
        return withContext(CoroutineScope(IO).coroutineContext) {
            dao.getAllByUserId(userId)
        }
    }

    suspend fun getAllByStoryId(userId: String, storyId: String): List<Task> {
        return withContext(CoroutineScope(IO).coroutineContext) {
            dao.getAllByStoryId(userId, storyId)
        }
    }

    fun updateTaskStatus(taskId: String, status: Boolean) {
        CoroutineScope(IO).launch {
            dao.updateTaskStatus(taskId, status)
        }
    }
}