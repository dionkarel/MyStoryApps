package com.example.mystoryapps.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mystoryapps.repository.StoriesRepository

class MapsStoriesViewModel (private val storiesRepository: StoriesRepository): ViewModel() {
    fun getStoryWithLocation() = storiesRepository.getStoryWithLocation()
}