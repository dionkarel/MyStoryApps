package com.example.mystoryapps.response

import com.google.gson.annotations.SerializedName

class AddStoryResponse (
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)