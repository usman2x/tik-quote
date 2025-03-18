package com.example.composeactivity.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Query("SELECT * FROM quotes")
    fun getAllQuotes(): Flow<List<QuoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotes(quotes: List<QuoteEntity>)

    @Query("UPDATE quotes SET isLiked = :isLiked WHERE id = :id")
    suspend fun updateLikeStatus(id: Int, isLiked: Boolean)

    @Query("UPDATE quotes SET isSaved = :isSaved WHERE id = :id")
    suspend fun updateSaveStatus(id: Int, isSaved: Boolean)

    @Query("DELETE FROM quotes")
    suspend fun deleteAllQuotes()

    @Query("SELECT DISTINCT category FROM quotes")
    suspend fun getCategories(): List<String>
}