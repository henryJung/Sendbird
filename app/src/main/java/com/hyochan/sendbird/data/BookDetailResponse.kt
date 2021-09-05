package com.hyochan.sendbird.data

import java.io.Serializable

data class BookDetailResponse(
    val error: String = "",
    val title: String,
    val subtitle: String,
    val authors: String = "",
    val publisher: String = "",
    val language: String = "",
    val isbn10: String = "",
    val isbn13: String,
    val pages: String = "0",
    val year: String = "",
    val rating: String = "0",
    val desc: String = "",
    val price: String,
    val image: String,
    val url: String,
) : Serializable
