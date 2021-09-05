package com.hyochan.sendbird.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hyochan.sendbird.repository.BookRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.NullPointerException

class DetailViewModel(private val bookRepository: BookRepository) : ViewModel() {
    val intent = Channel<DetailIntent>()
    val state: StateFlow<DetailState> get() = _state

    private val _state = MutableStateFlow<DetailState>(DetailState.Idle)

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeEach {
                when (it) {
                    is DetailIntent.Load -> load(it.isbn13)
                }
            }
        }
    }

    private suspend fun load(isbn13: String) {
        _state.value = DetailState.Loading
        viewModelScope.launch {
            _state.value = try {
                val response = bookRepository.getDetail(isbn13)
                if (response.error == "0") {
                    DetailState.LoadComplete(response)
                } else {
                    DetailState.Error(NullPointerException("error"))
                }
            } catch (e: Exception) {
                DetailState.Error(e)
            }
        }
    }
}

class DetailViewModelFactory(
    private val bookRepository: BookRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(bookRepository) as T
    }
}