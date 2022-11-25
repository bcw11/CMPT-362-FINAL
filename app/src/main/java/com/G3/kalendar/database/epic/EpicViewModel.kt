package com.G3.kalendar.database.epic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EpicViewModel(
    private val epicRepository: EpicRepository,
    private val userId: String
) : ViewModel() {
    private val _epics = MutableLiveData<List<Epic>>()
    val epics: LiveData<List<Epic>> = _epics

    fun insert(epic: Epic) {
        epic.userId = userId
        epicRepository.insert(epic)
    }

    fun getAllById() {
        _epics.value = epicRepository.getAllByUserId(userId)
    }

}