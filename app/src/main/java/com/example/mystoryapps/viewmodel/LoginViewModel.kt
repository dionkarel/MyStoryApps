package com.example.mystoryapps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapps.repository.StoriesRepository
import com.example.mystoryapps.response.LoginResultResponse
import kotlinx.coroutines.launch

class LoginViewModel (private val storiesRepository: StoriesRepository): ViewModel() {
    fun userLogin(email: String, password: String) = storiesRepository.userLogin(email, password)
}
