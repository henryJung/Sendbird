package com.hyochan.sendbird.ui.detail

import com.hyochan.sendbird.data.BookDetailResponse

sealed class DetailState {
    object Idle : DetailState()
    object Loading : DetailState()
    data class LoadComplete(val response: BookDetailResponse) : DetailState()
    data class Error(val throwable: Throwable) : DetailState()
}