package com.example.mystoryapps.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mystoryapps.data.entity.RemoteKeys
import com.example.mystoryapps.data.entity.StoriesEntity

@Database(
    entities = [StoriesEntity::class, RemoteKeys::class],
    version = 4,
    exportSchema = false
)
abstract class StoriesDb : RoomDatabase() {

    abstract fun storiesDao(): StoriesDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: StoriesDb? = null

        @JvmStatic
        fun getDatabase(context: Context): StoriesDb {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoriesDb::class.java, "stories"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}