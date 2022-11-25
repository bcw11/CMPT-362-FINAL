package com.G3.kalendar.database.epic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EpicViewModelFactory(private val repository: EpicRepository, private val userId: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EpicViewModel::class.java))
            return EpicViewModel(repository, userId) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}