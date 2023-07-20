package com.example.mystoryapps.response

import com.example.mystoryapps.data.entity.StoriesEntity
import com.google.gson.annotations.SerializedName

class GetStoryResponse (
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("listStory")
    val listStory: List<StoriesEntity>
)
