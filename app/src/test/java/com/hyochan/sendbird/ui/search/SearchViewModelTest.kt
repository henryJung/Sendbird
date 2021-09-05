package com.hyochan.sendbird.ui.search

import androidx.paging.PagingData
import com.hyochan.sendbird.CoroutinesTestRule
import com.hyochan.sendbird.data.BookDetailResponse
import com.hyochan.sendbird.data.Books
import com.hyochan.sendbird.repository.BookRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        viewModel = SearchViewModel(FakeBookRepository())
    }

    @Test
    fun searchTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val job = launch {
            viewModel.intent.send(SearchIntent.Search("java"))
        }
        assertTrue(viewModel.state.value is SearchState.ListSuccess)
        job.cancel()
    }

    inner class FakeBookRepository : BookRepository {
        override suspend fun getList(
            scope: CoroutineScope,
            query: String
        ): Flow<PagingData<Books>> {
            return flow {
                emit(PagingData.empty<Books>())
            }
        }

        override suspend fun getDetail(isbn13: String): BookDetailResponse {
            return BookDetailResponse("", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
        }
    }
}