package com.example.foodsharingapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodsharingapp.data.Repository
import com.example.foodsharingapp.data.SettingsRepository

// creates viewmodel and supplies their repository dependencies

class ViewModelFactory(
    private val repo: Repository,
    private val settings: SettingsRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repo) as T
        modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(settings) as T
        else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}