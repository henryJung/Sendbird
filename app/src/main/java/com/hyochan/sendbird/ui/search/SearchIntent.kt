package com.hyochan.sendbird.ui.search

sealed class SearchIntent {
    data class Search(val query: String) : SearchIntent()
}