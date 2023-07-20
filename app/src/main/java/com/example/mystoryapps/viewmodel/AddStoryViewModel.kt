package com.example.mystoryapps.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mystoryapps.repository.StoriesRepository
import com.google.android.gms.maps.model.LatLng
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel (private val storiesRepository: StoriesRepository): ViewModel() {
    fun addNewStory(file: MultipartBody.Part, description: RequestBody, latLng: LatLng?) = storiesRepository.addNewStory(file, description, latLng)
}