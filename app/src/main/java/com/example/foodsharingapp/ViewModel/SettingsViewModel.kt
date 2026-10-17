package com.example.foodsharingapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodsharingapp.data.AppTheme
import com.example.foodsharingapp.data.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsRepo: SettingsRepository) : ViewModel() {
    val theme: StateFlow<AppTheme> = settingsRepo.theme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppTheme.PEACH)
    val cameraOn: StateFlow<Boolean> = settingsRepo.cameraOn
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    val mapOn: StateFlow<Boolean> = settingsRepo.mapOn
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun setTheme(theme: AppTheme) { viewModelScope.launch { settingsRepo.setTheme(theme) } }
    fun setCameraOn(on: Boolean) { viewModelScope.launch { settingsRepo.setCameraOn(on) } }
    fun setMapOn(on: Boolean) { viewModelScope.launch { settingsRepo.setMapOn(on) } }
}