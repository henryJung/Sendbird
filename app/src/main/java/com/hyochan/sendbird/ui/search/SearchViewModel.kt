package com.hyochan.sendbird.ui.search

import androidx.lifecycle.*
import com.hyochan.sendbird.repository.BookRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchViewModel(private val bookRepository: BookRepository) : ViewModel() {
    val intent = Channel<SearchIntent>()
    val state: StateFlow<SearchState> get() = _state

    private val _state = MutableStateFlow<SearchState>(SearchState.Idle)
    private val viewModelJob = SupervisorJob()
    private val networkScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    init {
        handleIntent()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeEach {
                when (it) {
                    is SearchIntent.Search -> search(it.query)
                }
            }
        }
    }

    private suspend fun search(query: String) {
        viewModelScope.launch {
            _state.value = SearchState.ListSuccess(bookRepository.getList(networkScope, query))
        }
    }
}

class SearchViewModelFactory(
    private val bookRepository: BookRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(bookRepository) as T
    }
}