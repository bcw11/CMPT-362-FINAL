package com.G3.kalendar.database.story

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class StoryRepository(private val dao: StoryDao) {

    fun getAllByUserId(userId: String): List<Story> {
        return runBlocking {
            dao.getAllByUserId(userId)
        }
    }

    fun getAllByEpicId(userId: String, epicId: String): List<Story> {
        return runBlocking {
            dao.getAllByEpicId(userId, epicId)
        }
    }

    fun insert(story: Story) {
        CoroutineScope(IO).launch {
            dao.insert(story)
        }
    }
}