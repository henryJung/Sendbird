package com.hyochan.sendbird.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hyochan.sendbird.api.BookApi
import com.hyochan.sendbird.network.RetrofitBuilder
import com.hyochan.sendbird.ui.search.list.SearchPagingSource
import kotlinx.coroutines.*

class BookRepositoryImpl(
    private val apiDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BookRepository {
    private val bookApi = RetrofitBuilder.getRetrofit().create(BookApi::class.java)

    override suspend fun getList(
        scope: CoroutineScope,
        query: String
    ) = Pager(
            config = PagingConfig(
                pageSize = 1,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchPagingSource(scope, apiDispatcher, bookApi, query)
            }
        ).flow

    override suspend fun getDetail(isbn13: String) = withContext(apiDispatcher) {
        bookApi.getDetail(isbn13)
    }
}