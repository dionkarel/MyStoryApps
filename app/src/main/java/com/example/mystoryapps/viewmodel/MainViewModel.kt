package com.example.mystoryapps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapps.data.entity.StoriesEntity
import com.example.mystoryapps.repository.StoriesRepository

class MainViewModel (private val storiesRepository: StoriesRepository) : ViewModel() {
    fun getStories(): LiveData<PagingData<StoriesEntity>> {
        return storiesRepository.getStory().cachedIn(viewModelScope)
    }
}