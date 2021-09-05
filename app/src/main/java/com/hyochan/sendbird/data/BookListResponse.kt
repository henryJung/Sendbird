package com.hyochan.sendbird.data

import java.io.Serializable

data class BookListResponse(
    val error: String,
    val total: Long,
    val page: Int,
    val books: List<Books>? = null,
) : Serializable

data class Books(
    val title: String,
    val subtitle: String,
    val isbn13: String,
    val price: String,
    val image: String,
    val url: String
) : Serializable