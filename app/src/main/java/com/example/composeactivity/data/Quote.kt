package com.example.composeactivity.data

data class Quote(
    val id: Int,
    val quote: String,
    val author: String,
    val category: String,
    var isLiked: Boolean = false,
    var isSaved: Boolean = false,
    val lastUpdated: Long = 0
)
data class QuotesResponse(val quotes: List<Quote>)