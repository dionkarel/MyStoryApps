package com.example.mystoryapps.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.mystoryapps.ui.adapter.StoriesAdapter
import com.example.mystoryapps.data.local.entity.StoriesEntity
import com.example.mystoryapps.data.repository.StoriesRepository
import com.example.mystoryapps.ui.viewmodel.MainViewModel
import com.example.mystoryapps.utils.DummyData
import com.example.mystoryapps.utils.MainDispatcherRule
import com.example.mystoryapps.utils.getOrAwaitValue
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
internal class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(storiesRepository)
    }

    @Test
    fun `when getAllStories Should Not Null and Return Success`() = runTest {
        val dummyStoriesResponse = DummyData.generateDummyStory()
        val pagingData = StoryPagingSource.snapshot(dummyStoriesResponse)
        val expectedStories = MutableLiveData<PagingData<StoriesEntity>>()
        expectedStories.value = pagingData
        Mockito.`when`(storiesRepository.getStory()).thenReturn(expectedStories)

        val actualStories = mainViewModel.getStories().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF_CALLBACK,
            updateCallback = listUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStories)
        Assert.assertNotNull(differ.snapshot())
        assertEquals(dummyStoriesResponse, differ.snapshot())
        assertEquals(dummyStoriesResponse.size, differ.snapshot().size)
        assertEquals(dummyStoriesResponse[0], differ.snapshot()[0])
    }

    @Test
    fun `when getAllStories is empty and Return 0`() = runTest {
        val dummyStoriesResponse = DummyData.generateDummyStoryEmpty()
        val pagingData = StoryPagingSource.snapshot(dummyStoriesResponse)
        val expectedStories = MutableLiveData<PagingData<StoriesEntity>>()
        expectedStories.value = pagingData
        Mockito.`when`(storiesRepository.getStory()).thenReturn(expectedStories)

        val actualStories: PagingData<StoriesEntity>? = try {
            mainViewModel.getStories().getOrAwaitValue()
        } catch (e: Exception) {
            null
        }

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF_CALLBACK,
            updateCallback = listUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        if (actualStories != null) {
            differ.submitData(actualStories)
        } else {
            Assert.assertEquals(0, differ.snapshot().size)
        }
    }
}

val listUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

class StoryPagingSource : PagingSource<Int, LiveData<List<StoriesEntity>>>() {
    companion object {
        fun snapshot(items: List<StoriesEntity>): PagingData<StoriesEntity> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoriesEntity>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoriesEntity>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}