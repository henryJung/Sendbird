package com.hyochan.sendbird.ui.search.list

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadParams.Refresh
import androidx.paging.PagingSource.LoadResult.Page
import com.hyochan.sendbird.CoroutinesTestRule
import com.hyochan.sendbird.api.BookApi
import com.hyochan.sendbird.data.BookDetailResponse
import com.hyochan.sendbird.data.BookListResponse
import com.hyochan.sendbird.data.Books
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SearchPagingSourceTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var bookApi: BookApi

    private val mockApi = FakeBookApi()

    @Before
    fun setup() {
    }

    @Test
    fun searchPagingSourceData() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val pagingSource = SearchPagingSource(this, coroutinesTestRule.testDispatcher, mockApi, "success")

        assertEquals(
            Page(
                data = listOf(book, book),
                prevKey = null,
                nextKey = 2
            ),
            pagingSource.load(
                Refresh(
                    key = null,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun searchPagingSourceData1() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val pagingSource = SearchPagingSource(this, coroutinesTestRule.testDispatcher, bookApi, "fail")

        Mockito.`when`(bookApi.getList("fail", 0)).thenReturn(null)

        val expectedResult = PagingSource.LoadResult.Error<Int, Books>(NullPointerException())

        assertEquals(
            expectedResult.toString(), pagingSource.load(
                Refresh(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            ).toString()
        )
    }

    private val book = Books("", "", "", "", "", "")
    private val bookListResponse = BookListResponse("",2, 1, listOf(book, book))
    private val detailResponse = BookDetailResponse("", "", "", "", "", "", "", "", "", "", "", "", "", "", "")

    inner class FakeBookApi : BookApi {

        override suspend fun getList(query: String, page: Int): BookListResponse {
            return bookListResponse
        }

        override suspend fun getDetail(isbn13: String): BookDetailResponse {
            return detailResponse
        }
    }
}