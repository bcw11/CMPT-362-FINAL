package com.G3.kalendar.database.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StoryViewModelFactory(private val repository: StoryRepository, private val userId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java))
            return StoryViewModel(repository, userId) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}