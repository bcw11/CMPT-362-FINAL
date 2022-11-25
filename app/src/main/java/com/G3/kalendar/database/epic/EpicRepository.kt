package com.G3.kalendar.database.epic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EpicRepository(private val dao: EpicDao) {

    fun insert(epic: Epic) {
        CoroutineScope(IO).launch {
            dao.insert(epic)
        }
    }

    fun getAllByUserId(userId: String): List<Epic> {
        return runBlocking {
            dao.getAllByUserId(userId)
        }
    }
}