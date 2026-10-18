package com.example.foodsharingapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodsharingapp.data.FoodWithPlace
import com.example.foodsharingapp.data.Repository
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

    fun delete(item: FoodWithPlace) = viewModelScope.launch { repo.deleteFood(item.food) }

    fun saveFood(
        photo: String,
        description: String,
        tags: List<String>,
        place: com.example.foodsharingapp.data.Place?
    ) = viewModelScope.launch {
        repo.saveFood(photo, description, tags, place)
    }

    fun updateFood(
        existing: com.example.foodsharingapp.data.MyFoodRecord,
        photo: String,
        description: String,
        tags: List<String>,
        place: com.example.foodsharingapp.data.Place?
    ) = viewModelScope.launch {
        repo.updateFood(existing, photo, description, tags, place, clearPlace = (place == null))
    }
}
