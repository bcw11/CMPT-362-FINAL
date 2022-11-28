package com.G3.kalendar.database.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TaskViewModel(
    private val taskRepository: TaskRepository,
    private val userId: String
) : ViewModel() {
    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    fun insert(task: Task) {
        task.userId = userId
        taskRepository.insert(task)
    }

    fun getAllById() {
        viewModelScope.launch {
            _tasks.value = taskRepository.getAllByUserId(userId)
        }
    }

    fun getAllByStoryId(storyId: String) {
        viewModelScope.launch {
            _tasks.value = taskRepository.getAllByStoryId(userId, storyId)
        }
    }

    fun updateTaskStatus(taskId: String, status: Boolean) {
        taskRepository.updateTaskStatus(taskId, status)
    }

    fun deleteTask(taskId: String) {
        taskRepository.deleteTask(taskId)
    }
}