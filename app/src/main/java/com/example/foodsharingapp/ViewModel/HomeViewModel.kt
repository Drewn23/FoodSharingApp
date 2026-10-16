package com.example.foodsharingapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: Repository) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val foods: StateFlow<List<FoodWithPlace>> = _searchText
        .flatMapLatest { q -> if (q.isBlank()) repo.allFood else repo.searchByTag(q) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchChange(text: String) { _searchText.value = text }

    fun toggleFavorite(item: FoodWithPlace) = viewModelScope.launch {
        repo.setFavorite(item.food, !item.food.isFavorite)
    }

    fun delete(item: FoodWithPlace) = viewModelScope.launch { repo.deleteFood(item.food) }

    // TEMPORARY: for the Step 10 test, delete later
    fun addTestEntry() = viewModelScope.launch {
        repo.saveFood("test.jpg", "Nasi lemak", listOf("@Breakfast"), null)
    }
}