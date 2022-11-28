package com.G3.kalendar.database.epic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EpicRepository(private val dao: EpicDao) {

    fun insert(epic: Epic) {
        CoroutineScope(IO).launch {
            dao.insert(epic)
        }
    }

    suspend fun getAllByUserId(userId: String): List<Epic> {
        return withContext(CoroutineScope(IO).coroutineContext) {
            dao.getAllByUserId(userId)
        }
    }

    fun delete(epicId: String) {
        CoroutineScope(IO).launch {
            dao.delete(epicId)
        }
    }
}