package com.hyochan.sendbird.repository

import androidx.paging.PagingData
import com.hyochan.sendbird.CoroutinesTestRule
import com.hyochan.sendbird.data.BookDetailResponse
import com.hyochan.sendbird.data.Books
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookRepositoryTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private lateinit var bookRepository: BookRepository

    private val pagingData = flow {
        emit(PagingData.empty<Books>())
    }

    private val detailResponse = BookDetailResponse("", "", "", "", "", "", "", "", "", "", "", "", "", "", "")

    @Before
    fun setUp() {
        bookRepository = FakeBookRepository()
    }

    @Test
    fun getListTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val result = bookRepository.getList(this, "test")
        assertEquals(result, pagingData)
    }

    @Test
    fun getDetailTest() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val result = bookRepository.getDetail("isbn13")
        assertEquals(result, detailResponse)
    }

    inner class FakeBookRepository : BookRepository {
        override suspend fun getList(
            scope: CoroutineScope,
            query: String
        ): Flow<PagingData<Books>> {
            return pagingData
        }

        override suspend fun getDetail(isbn13: String): BookDetailResponse {
            return detailResponse
        }
    }
}