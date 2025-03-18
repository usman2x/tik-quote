package com.example.composeactivity.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuoteRepository(private val quoteDao: QuoteDao, private val api: QuoteApi) {

    fun getQuotesFromDB(): Flow<List<Quote>> {
        return quoteDao.getAllQuotes().map { list -> list.map { it.toQuote() } }
    }

    suspend fun getQuotesFromAPI(): List<Quote> {
        return try {
            api.getQuotes().quotes
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun saveQuotesToDB(quotes: List<Quote>) {
        quoteDao.insertQuotes(quotes.map { it.toQuoteEntity() })
    }

    suspend fun toggleLike(quote: Quote) {
        quoteDao.updateLikeStatus(quote.id, !quote.isLiked)
    }

    suspend fun toggleSave(quote: Quote) {
        quoteDao.updateSaveStatus(quote.id, !quote.isSaved)
    }

    suspend fun getCategories(): List<String> {
        val categories = quoteDao.getCategories()
        return listOf("All") + categories
    }

    suspend fun clearQuotesFromDB() {
        quoteDao.deleteAllQuotes()
    }
}
