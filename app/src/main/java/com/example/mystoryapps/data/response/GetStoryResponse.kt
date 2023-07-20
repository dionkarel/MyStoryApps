package com.example.mystoryapps.data.response

import com.example.mystoryapps.data.local.entity.StoriesEntity
import com.google.gson.annotations.SerializedName

data class GetStoryResponse (
    @field:SerializedName("listStory")
    val listStory: List<StoriesEntity>,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
