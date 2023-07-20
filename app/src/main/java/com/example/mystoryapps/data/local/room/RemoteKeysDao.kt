package com.example.mystoryapps.data.local.room

import androidx.room.*
import com.example.mystoryapps.data.local.entity.RemoteKeys

@Dao
interface RemoteKeysDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteRemoteKeys()
}

