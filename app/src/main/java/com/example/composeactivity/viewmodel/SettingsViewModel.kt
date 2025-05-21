package com.example.composeactivity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeactivity.data.DataStoreManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val dataStore: DataStoreManager,
    private val allCategories: List<String>
) : ViewModel() {

    private val _selectedCategories = MutableStateFlow<Set<String>>(emptySet())
    val selectedCategories: StateFlow<Set<String>> = _selectedCategories.asStateFlow()


    private var hasInitialized = false

    init {
        viewModelScope.launch {
            dataStore.selectedCategoriesFlow.collect { stored ->
                if (!hasInitialized) {
                    _selectedCategories.value = if (stored.isEmpty()) allCategories.toSet() else stored
                    hasInitialized = true
                } else {
                    _selectedCategories.value = stored
                }
            }
        }
    }

    fun toggleCategory(category: String) {
        val current = _selectedCategories.value.toMutableSet()
        if (current.contains(category)) {
            current.remove(category)
        } else {
            current.add(category)
        }
        saveCategories(current)
    }

    fun setAllCategories(selected: Boolean) {
        val updated = if (selected) allCategories.toSet() else emptySet()
        saveCategories(updated)
    }

    private fun saveCategories(categories: Set<String>) {
        _selectedCategories.value = categories
        viewModelScope.launch {
            dataStore.saveSelectedCategories(categories)
        }
    }
}
