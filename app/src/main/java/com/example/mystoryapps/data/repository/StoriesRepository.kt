package com.example.mystoryapps.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.example.mystoryapps.data.remote.StoriesRemoteMediator
import com.example.mystoryapps.data.local.entity.StoriesEntity
import com.example.mystoryapps.data.response.AddStoryResponse
import com.example.mystoryapps.data.response.GetStoryResponse
import com.example.mystoryapps.data.response.LoginResponse
import com.example.mystoryapps.data.response.RegisterResponse
import com.example.mystoryapps.data.local.room.StoriesDb
import com.example.mystoryapps.network.ApiService
import com.example.mystoryapps.data.response.*
import com.example.mystoryapps.utils.Result
import com.google.android.gms.maps.model.LatLng
import okhttp3.MultipartBody
import okhttp3.RequestBody


class StoriesRepository (private val storiesDb: StoriesDb, private val apiService: ApiService) {

    fun userRegister(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.userRegister(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("RegisterViewModel", "userRegister: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun userLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.userLogin(email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("LoginViewModel", "userLogin: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStory(): LiveData<PagingData<StoriesEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoriesRemoteMediator(storiesDb, apiService),
            pagingSourceFactory = {
                storiesDb.storiesDao().getAllStory()
            }
        ).liveData
    }


    fun addNewStory(file: MultipartBody.Part, description: RequestBody, latLng: LatLng?): LiveData<Result<AddStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.addStory(file, description, latLng?.latitude, latLng?.longitude)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("AddStoryViewModel", "userUpload: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStoryWithLocation(): LiveData<Result<GetStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoryWithLocation(100)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("MapsStoriesViewModel", "getStoriesWithLocation: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

}