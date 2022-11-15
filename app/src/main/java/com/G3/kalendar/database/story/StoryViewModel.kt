package com.G3.kalendar.database.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StoryViewModel(private val repository: StoryRepository) : ViewModel() {
    private val _stories = MutableLiveData<List<Story>>()
    val stories: LiveData<List<Story>> = _stories

    fun insert(story: Story) {
        repository.insert(story)
    }

    fun getAllById(id: String) {
        _stories.value = repository.getAllById(id)
    }

    fun getAll() {
        _stories.value = repository.getAll()
    }
}

class StoryViewModelFactory(private val repository: StoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java))
            return StoryViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}