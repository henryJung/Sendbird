package com.hyochan.sendbird.ui.detail

sealed class DetailIntent {
    data class Load(val isbn13: String) : DetailIntent()
}