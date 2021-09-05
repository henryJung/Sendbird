package com.hyochan.sendbird.api

import com.hyochan.sendbird.data.BookDetailResponse
import com.hyochan.sendbird.data.BookListResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface BookApi {

    @GET("search/{query}/{page}")
    suspend fun getList(@Path("query") query: String, @Path("page") page: Int): BookListResponse

    @GET("books/{isbn13}")
    suspend fun getDetail(@Path("isbn13") isbn13: String): BookDetailResponse
}