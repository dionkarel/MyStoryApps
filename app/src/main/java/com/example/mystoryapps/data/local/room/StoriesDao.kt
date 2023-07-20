package com.example.mystoryapps.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mystoryapps.data.local.entity.StoriesEntity

@Dao
interface StoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<StoriesEntity>)

    @Query("SELECT * FROM stories")
    fun getAllStory(): PagingSource<Int, StoriesEntity>

    @Query("DELETE FROM stories")
    suspend fun deleteAll()
}
