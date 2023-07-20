package com.example.mystoryapps.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.mystoryapps.UserPreference
import com.example.mystoryapps.data.room.StoriesDb
import com.example.mystoryapps.network.ApiConfig
import com.example.mystoryapps.repository.StoriesRepository

object Injection {
    fun provideRepository(context: Context): StoriesRepository {
        val database = StoriesDb.getDatabase(context)
        val apiService = ApiConfig.getApiService(context)
        return StoriesRepository(database, apiService)
    }
}