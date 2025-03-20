package com.example.composeactivity.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class QuoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val quote: String,
    val author: String,
    val category: String,
    var isLiked: Boolean = false,
    var isSaved: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    fun toQuote() = Quote(id, quote, author, category, isLiked, isSaved, lastUpdated)
}

fun Quote.toQuoteEntity() = QuoteEntity(id, quote, author, category, isLiked, isSaved, lastUpdated)
