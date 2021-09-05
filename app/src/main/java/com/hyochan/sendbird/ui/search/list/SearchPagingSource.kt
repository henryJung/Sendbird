package com.hyochan.sendbird.ui.search.list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hyochan.sendbird.api.BookApi
import com.hyochan.sendbird.data.Books
import com.hyochan.sendbird.util.QueryUtil
import kotlinx.coroutines.*

class SearchPagingSource(
    private val scope: CoroutineScope,
    private val apiDispatcher: CoroutineDispatcher,
    private val bookApi: BookApi,
    private val query: String,
) : PagingSource<Int, Books>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Books> {
        val page = params.key ?: 1
        return try {
            withContext(apiDispatcher) {
                val (orQuery, notQuery) = QueryUtil.parsQuery(query)
                val orResultJob = scope.async { orQuery.search(page) }
                val notResultJob = scope.async { notQuery.search(page) }

                val resultList = orResultJob.await() - notResultJob.await()
                LoadResult.Page(
                    resultList,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (resultList.isEmpty()) null else page + 1
                )
            }
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Books>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    private suspend fun Set<String>.search(page: Int): List<Books> {
        this.map { queryString ->
            scope.async {
                bookApi.getList(queryString, page)
            }
        }.awaitAll().forEach { response ->
            if (!response.books.isNullOrEmpty()) {
                return response.books
            }
        }
        return emptyList()
    }
}