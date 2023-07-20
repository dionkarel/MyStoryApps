package com.example.mystoryapps.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.mystoryapps.data.local.entity.RemoteKeys
import com.example.mystoryapps.data.local.entity.StoriesEntity
import com.example.mystoryapps.data.local.room.StoriesDb
import com.example.mystoryapps.network.ApiService

@OptIn(ExperimentalPagingApi::class)
class StoriesRemoteMediator (
    private val storiesDb: StoriesDb,
    private val apiService: ApiService
): RemoteMediator<Int, StoriesEntity>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoriesEntity>
    ) : MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH ->{
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = apiService.getStories(page, state.config.pageSize, ).listStory

            val endOfPaginationReached = responseData.isEmpty()

            storiesDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    storiesDb.remoteKeysDao().deleteRemoteKeys()
                    storiesDb.storiesDao().deleteAll()
                }
            }
            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (endOfPaginationReached) null else page + 1
            val keys = responseData.map {
                RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
            }
            storiesDb.remoteKeysDao().insertAll(keys)
            storiesDb.storiesDao().insertStory(responseData)
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoriesEntity>): RemoteKeys? {
        return state.pages.lastOrNull {it.data.isNotEmpty()}?.data?.lastOrNull()?.let { data ->
            storiesDb.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoriesEntity>): RemoteKeys? {
        return state.pages.firstOrNull {it.data.isNotEmpty()}?.data?.firstOrNull()?.let { data ->
            storiesDb.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoriesEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                storiesDb.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }
}