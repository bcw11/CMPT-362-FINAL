package com.G3.kalendar.database.story

import com.G3.kalendar.database.task.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoryRepository(private val dao: StoryDao) {

    suspend fun getAllByUserId(userId: String): List<Story> {
        return withContext(CoroutineScope(IO).coroutineContext) {
            dao.getAllByUserId(userId)
        }
    }

    suspend fun getAllByEpicId(userId: String, epicId: String): List<Story> {
        return withContext(CoroutineScope(IO).coroutineContext) {
            dao.getAllByEpicId(userId, epicId)
        }
    }

    suspend fun getAllByStatus(userId: String, status: String): List<Story> {
        return withContext(CoroutineScope(IO).coroutineContext) {
            dao.getAllByStatus(userId, status)
        }
    }

    suspend fun getAllByStatusAndEpicId(userId: String, status: String, epicId: String): List<Story> {
        return withContext(CoroutineScope(IO).coroutineContext) {
            dao.getAllByStatusAndEpicId(userId, status, epicId)
        }
    }

    fun insert(story: Story) {
        CoroutineScope(IO).launch {
            dao.insert(story)
        }
    }

    fun insertWithTasks(story: Story, taskList: ArrayList<Task>) {
        CoroutineScope(IO).launch {
            dao.insertWithTasks(story, taskList)
        }
    }

    fun delete(story: Story) {
        CoroutineScope(IO).launch {
            dao.delete(story)
        }
    }

    fun update(story: Story) {
        CoroutineScope(IO).launch {
            dao.update(story)
        }
    }
}