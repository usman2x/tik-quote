package com.example.composeactivity.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class QuoteRepository(private val quoteDao: QuoteDao, private val api: QuoteApi) {

    fun getQuotesFromDB(): Flow<List<Quote>> {
        return quoteDao.getAllQuotes().map { list -> list.map { it.toQuote() } }
    }

    fun getLikedQuotesFromDB(): Flow<List<Quote>> {
        return quoteDao.getAllLikedQuotes().map { list -> list.map { it.toQuote() } }
    }

    fun getSavedQuotesFromDB(): Flow<List<Quote>> {
        return quoteDao.getAllSavedQuotes().map { list -> list.map { it.toQuote() } }
    }

    suspend fun getQuotesFromAPI(): List<Quote> {
        return try {
            api.getQuotes().quotes
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateQuotesInDB(apiQuotes: List<Quote>) {
        val existingQuotes = quoteDao.getAllQuotes().first().associateBy { it.id }

        val quotesToInsertOrUpdate = apiQuotes.map { apiQuote ->
            val existingQuote = existingQuotes[apiQuote.id]

            if (existingQuote == null) {
                apiQuote.toQuoteEntity()
            } else if (existingQuote.lastUpdated < apiQuote.lastUpdated) {
                existingQuote.copy(
                    quote = apiQuote.quote,
                    author = apiQuote.author,
                    category = apiQuote.category,
                    lastUpdated = apiQuote.lastUpdated
                )
            } else {
                existingQuote
            }
        }

        quoteDao.insertQuotes(quotesToInsertOrUpdate)
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

    suspend fun searchQuotesFromDB(query: String): List<Quote> {
        return quoteDao.searchQuotes(query)
    }
}
