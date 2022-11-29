package com.G3.kalendar.database.story

import androidx.lifecycle.*
import com.G3.kalendar.database.task.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class StoryViewModel(private val repository: StoryRepository, private val userId: String) : ViewModel() {
    private val _stories = MutableLiveData<List<Story>>()
    val stories: LiveData<List<Story>> = _stories

    init {
        viewModelScope.launch {
            _stories.value = repository.getAllByUserId(userId)
        }
    }

    fun insert(story: Story) {
        story.userId = userId
        repository.insert(story)
    }

    fun insertWithTasks(story: Story, taskList: ArrayList<Task>) {
        story.userId = userId
        for (task in taskList) task.userId = userId
        repository.insertWithTasks(story, taskList)
    }

    fun getAllById() {
        viewModelScope.launch {
            _stories.value = repository.getAllByUserId(userId)
        }
    }

    fun getAllByEpicId(epicId: String) {
        viewModelScope.launch {
            _stories.value = repository.getAllByEpicId(userId, epicId)
        }
    }

    fun delete(story: Story) {
        repository.delete(story)
    }

    fun update(story: Story) {
        repository.update(story)
    }
}