package com.example.mystoryapps.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "stories")
data class StoriesEntity (
    @PrimaryKey
    @field:ColumnInfo(name = "id")
    @field:SerializedName("id")
    val id: String,

    @field:ColumnInfo(name = "name")
    @field:SerializedName("name")
    val name: String,

    @field:ColumnInfo(name = "description")
    @field:SerializedName("description")
    val description: String,

    @field:ColumnInfo(name = "photoUrl")
    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:ColumnInfo(name = "createdAt")
    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:ColumnInfo(name = "lat")
    @field:SerializedName("lat")
    val lat: Double?,

    @field:ColumnInfo(name = "lon")
    @field:SerializedName("lon")
    val lon: Double?
)

@Entity(tableName = "remote_keys")
data class RemoteKeys(

    @PrimaryKey
    val id: String,

    val prevKey: Int?,

    val nextKey: Int?

)
