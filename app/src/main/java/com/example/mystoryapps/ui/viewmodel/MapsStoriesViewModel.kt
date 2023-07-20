package com.example.mystoryapps.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mystoryapps.data.repository.StoriesRepository

class MapsStoriesViewModel (private val storiesRepository: StoriesRepository): ViewModel() {
    fun getStoryWithLocation() = storiesRepository.getStoryWithLocation()
}