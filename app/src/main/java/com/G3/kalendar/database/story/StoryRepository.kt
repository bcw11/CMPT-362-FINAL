package com.G3.kalendar.database.story

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class StoryRepository(private val dao: StoryDao) {

    fun getAllById(userId: String): List<Story> {
        return runBlocking {
            dao.getAllById(userId)
        }
    }

    fun getAll(): List<Story> {
        return runBlocking {
            dao.getAll()
        }
    }

    fun insert(story: Story) {
        CoroutineScope(IO).launch {
            dao.insert(story)
        }
    }
}