package com.G3.kalendar.database.story

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class StoryViewModel(private val repository: StoryRepository, private val userId: String) : ViewModel() {
    private val _stories = MutableLiveData<List<Story>>()
    val stories: LiveData<List<Story>> = _stories

    fun insert(story: Story) {
        story.userId = userId
        repository.insert(story)
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
}