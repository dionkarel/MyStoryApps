package com.example.mystoryapps.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapps.data.local.entity.StoriesEntity
import com.example.mystoryapps.data.repository.StoriesRepository

class MainViewModel (private val storiesRepository: StoriesRepository) : ViewModel() {
    fun getStories(): LiveData<PagingData<StoriesEntity>> {
        return storiesRepository.getStory().cachedIn(viewModelScope)
    }
}