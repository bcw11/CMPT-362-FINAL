package com.G3.kalendar.database

import com.G3.kalendar.database.epic.EpicDao
import com.G3.kalendar.database.epic.EpicRepository
import com.G3.kalendar.database.epic.EpicViewModelFactory
import com.G3.kalendar.database.story.StoryDao
import com.G3.kalendar.database.story.StoryRepository
import com.G3.kalendar.database.story.StoryViewModelFactory
import com.G3.kalendar.database.task.TaskDao
import com.G3.kalendar.database.task.TaskRepository
import com.G3.kalendar.database.task.TaskViewModelFactory
import com.G3.kalendar.database.user.UserDao
import com.G3.kalendar.database.user.UserRepository
import com.G3.kalendar.database.user.UserViewModelFactory
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DatabaseViewModelFactory(
    userId: String
) {

    val epicViewModelFactory: EpicViewModelFactory
    val storyViewModelFactory: StoryViewModelFactory
    val taskViewModelFactory: TaskViewModelFactory
    val userViewModelFactory: UserViewModelFactory

    init {
        val db = Firebase.firestore

        val epicDao = EpicDao(db)
        val epicRepository = EpicRepository(epicDao)
        epicViewModelFactory = EpicViewModelFactory(epicRepository, userId)


        val storyDao = StoryDao(db)
        val storyRepository = StoryRepository(storyDao)
        storyViewModelFactory = StoryViewModelFactory(storyRepository, userId)

        val taskDao = TaskDao(db)
        val taskRepository = TaskRepository(taskDao)
        taskViewModelFactory = TaskViewModelFactory(taskRepository, userId)

        val userDao = UserDao(db)
        val userRepository = UserRepository(userDao)
        userViewModelFactory = UserViewModelFactory(userRepository)
    }
}