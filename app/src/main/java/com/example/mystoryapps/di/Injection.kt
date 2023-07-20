package com.example.mystoryapps.di

import android.content.Context
import com.example.mystoryapps.data.local.room.StoriesDb
import com.example.mystoryapps.network.ApiConfig
import com.example.mystoryapps.data.repository.StoriesRepository

object Injection {
    fun provideRepository(context: Context): StoriesRepository {
        val database = StoriesDb.getDatabase(context)
        val apiService = ApiConfig.getApiService(context)
        return StoriesRepository(database, apiService)
    }
}