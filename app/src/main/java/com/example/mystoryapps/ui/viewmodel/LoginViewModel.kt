package com.example.mystoryapps.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mystoryapps.data.repository.StoriesRepository

class LoginViewModel (private val storiesRepository: StoriesRepository): ViewModel() {
    fun userLogin(email: String, password: String) = storiesRepository.userLogin(email, password)
}
