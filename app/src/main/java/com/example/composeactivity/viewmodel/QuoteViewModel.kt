package com.example.composeactivity.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeactivity.data.Quote
import com.example.composeactivity.data.QuoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuoteViewModel(private val repository: QuoteRepository) : ViewModel() {
    private val _quotes = MutableStateFlow<List<Quote>>(emptyList())
    val quotes: StateFlow<List<Quote>> = _quotes.asStateFlow()

    private val _likedQuotes = MutableStateFlow<List<Quote>>(emptyList())
    val likedQuotes: StateFlow<List<Quote>> = _likedQuotes.asStateFlow()

    private val _savedQuotes = MutableStateFlow<List<Quote>>(emptyList())
    val savedQuotes: StateFlow<List<Quote>> = _savedQuotes.asStateFlow()

    var categories by mutableStateOf<List<String>>(emptyList())
        private set

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredQuotes: StateFlow<List<Quote>> = searchQuery
        .combine(_quotes) { query, quotes ->
            if (query.isBlank()) quotes
            else quotes.filter {
                it.quote.contains(query, ignoreCase = true) ||
                        it.author.contains(query, ignoreCase = true)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        observeQuotes()
        fetchQuotes()
        fetchLikedQuotes()
        fetchSavedQuotes()
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

    private fun fetchLikedQuotes() {
        viewModelScope.launch {
            repository.getLikedQuotesFromDB().collect { likedQuotes ->
                _likedQuotes.value = likedQuotes
            }
        }
    }

    private fun fetchSavedQuotes() {
        viewModelScope.launch {
            repository.getSavedQuotesFromDB().collect { savedQuotes ->
                _savedQuotes.value = savedQuotes
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

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}