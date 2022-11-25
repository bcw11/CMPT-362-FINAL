package com.G3.kalendar.database.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StoryViewModel(private val repository: StoryRepository, private val userId: String) : ViewModel() {
    private val _stories = MutableLiveData<List<Story>>()
    val stories: LiveData<List<Story>> = _stories

    fun insert(story: Story) {
        story.userId = userId
        repository.insert(story)
    }

    fun getAllById() {
        _stories.value = repository.getAllByUserId(userId)
    }

    fun getAllByEpicId(epicId: String) {
        _stories.value = repository.getAllByEpicId(userId, epicId)
    }
}