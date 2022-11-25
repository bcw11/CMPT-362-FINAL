package com.G3.kalendar.database.story

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

    fun insert(story: Story) {
        CoroutineScope(IO).launch {
            dao.insert(story)
        }
    }
}