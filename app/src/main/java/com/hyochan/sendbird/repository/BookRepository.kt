package com.hyochan.sendbird.repository

import androidx.paging.PagingData
import com.hyochan.sendbird.data.BookDetailResponse
import com.hyochan.sendbird.data.Books
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun getList(scope: CoroutineScope, query: String) : Flow<PagingData<Books>>

    suspend fun getDetail(isbn13: String) : BookDetailResponse
}