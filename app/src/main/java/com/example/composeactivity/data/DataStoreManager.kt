package com.example.composeactivity.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "user_preferences"
private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)

class DataStoreManager(private val context: Context) {

    companion object {
        val SELECTED_CATEGORIES = stringSetPreferencesKey("selected_categories")
    }

    val selectedCategoriesFlow: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[SELECTED_CATEGORIES] ?: emptySet()
    }

    suspend fun saveSelectedCategories(categories: Set<String>) {
        context.dataStore.edit { prefs ->
            prefs[SELECTED_CATEGORIES] = categories
        }
    }
}
