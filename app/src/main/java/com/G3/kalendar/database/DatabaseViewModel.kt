package com.G3.kalendar.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.G3.kalendar.database.epic.Epic
import com.G3.kalendar.database.epic.EpicRepository
import com.G3.kalendar.database.story.Story
import com.G3.kalendar.database.story.StoryRepository

class DatabaseViewModel(
    private val storyRepository: StoryRepository,
    private val epicRepository: EpicRepository,
    private val userId: String
) : ViewModel() {
    private val _stories = MutableLiveData<List<Story>>()
    val stories: LiveData<List<Story>> = _stories

    private val _epics = MutableLiveData<List<Epic>>()
    val epics: LiveData<List<Epic>> = _epics

    fun insertStory(story: Story) {
        storyRepository.insert(story)
    }

    fun getAllStoriesById(id: String) {
        _stories.value = storyRepository.getAllByUserId(id)
    }

    fun insertEpic(epic: Epic) {
        epicRepository.insert(epic)
    }

    fun getAllEpicsById(id: String) {
        _epics.value = epicRepository.getAllByUserId(id)
    }

}