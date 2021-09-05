package com.hyochan.sendbird.ui.search

import androidx.paging.PagingData
import com.hyochan.sendbird.data.Books
import kotlinx.coroutines.flow.Flow

sealed class SearchState {
    object Idle : SearchState()
    data class ListSuccess(val result: Flow<PagingData<Books>>) : SearchState()
}