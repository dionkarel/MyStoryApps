package com.example.mystoryapps.data.room

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mystoryapps.data.entity.StoriesEntity
import com.example.mystoryapps.response.StoriesResponse

@Dao
interface StoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<StoriesEntity>)

    @Query("SELECT * FROM stories")
    fun getAllStory(): PagingSource<Int, StoriesEntity>

    @Query("DELETE FROM stories")
    suspend fun deleteAll()
}
