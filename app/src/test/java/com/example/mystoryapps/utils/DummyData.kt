package com.example.mystoryapps.utils

import com.example.mystoryapps.data.local.entity.StoriesEntity
import com.example.mystoryapps.data.response.LoginResultResponse

object DummyData {
    fun generateDummyStory(): List<StoriesEntity> {
        val storyList = ArrayList<StoriesEntity>()
        for (i in 0..5) {
            val dummy = StoriesEntity(
                id = "coba$i",
                name = "coba $i",
                description = "Cerita $i",
                photoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/Android_logo_2019.png/800px-Android_logo_2019.png",
                createdAt = "2023-07-10T00:00:00Z",
                lat = null,
                lon = null
            )
            storyList.add(dummy)
        }
        return storyList
    }

    fun generateDummyStoryEmpty(): List<StoriesEntity> {
        return ArrayList()
    }

    fun generateDummyUserLoginResponse(): LoginResultResponse {
        return LoginResultResponse(
            "id",
            "name",
            "token"
        )
    }
}