package com.G3.kalendar.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserRepository(private val dao: UserDao) {

    fun getUser(id: String): User? {
        return runBlocking {
            dao.getUser(id)
        }
    }

    fun getAll(): List<User> {
        return runBlocking {
            dao.getAll()
        }
    }

    fun insert(user: User) {
        CoroutineScope(IO).launch {
            dao.insert(user)
        }
    }
}