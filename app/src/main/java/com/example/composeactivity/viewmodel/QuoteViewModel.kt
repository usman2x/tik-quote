package com.example.composeactivity.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeactivity.data.Quote
import com.example.composeactivity.data.RetrofitInstance
import kotlinx.coroutines.launch

class QuoteViewModel : ViewModel() {
    var quotes by mutableStateOf<List<Quote>>(emptyList())
        private set

    init {
        fetchQuotes()
    }

    private fun fetchQuotes() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getQuotes()
                quotes = response.quotes
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}