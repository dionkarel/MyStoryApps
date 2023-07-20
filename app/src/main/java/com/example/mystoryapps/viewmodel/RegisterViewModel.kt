package com.example.mystoryapps.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mystoryapps.repository.StoriesRepository

class RegisterViewModel (private val storiesRepository: StoriesRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) = storiesRepository.userRegister(name, email, password)
}
