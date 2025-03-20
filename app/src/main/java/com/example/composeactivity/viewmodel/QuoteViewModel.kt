package com.example.composeactivity.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeactivity.data.Quote
import com.example.composeactivity.data.QuoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuoteViewModel(private val repository: QuoteRepository) : ViewModel() {
    private val _quotes = MutableStateFlow<List<Quote>>(emptyList())
    val quotes: StateFlow<List<Quote>> = _quotes.asStateFlow()

    var categories by mutableStateOf<List<String>>(emptyList())
        private set

    init {
        observeQuotes()
        fetchQuotes()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            categories = repository.getCategories()
        }
    }

    private fun fetchQuotes() {
        viewModelScope.launch {
            val apiQuotes = repository.getQuotesFromAPI()
            if (apiQuotes.isNotEmpty()) {
                repository.updateQuotesInDB(apiQuotes)
                fetchCategories()
            }
        }
    }

    private fun observeQuotes() {
        viewModelScope.launch {
            repository.getQuotesFromDB().collect { updatedQuotes ->
                _quotes.value = updatedQuotes
                fetchCategories()
            }
        }
    }

    fun toggleLike(quote: Quote) {
        viewModelScope.launch {
            repository.toggleLike(quote)
        }
    }

    fun toggleSave(quote: Quote) {
        viewModelScope.launch {
            repository.toggleSave(quote)
        }
    }

    fun syncQuotes(onSyncComplete: () -> Unit) {
        viewModelScope.launch {
            val apiQuotes = repository.getQuotesFromAPI()
            if (apiQuotes.isNotEmpty()) {
                repository.updateQuotesInDB(apiQuotes)
                onSyncComplete()
            }
        }
    }
}