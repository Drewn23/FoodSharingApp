package com.example.foodsharingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodsharingapp.ViewModel.HomeViewModel
import com.example.foodsharingapp.ViewModel.ViewModelFactory
import com.example.foodsharingapp.data.AppTheme
import com.example.foodsharingapp.data.DataDb
import com.example.foodsharingapp.data.Repository
import com.example.foodsharingapp.data.SettingsRepository
import com.example.foodsharingapp.ui.theme.AppNav
import com.example.foodsharingapp.ui.theme.FoodSharingAppTheme

// MainActivity is the entry point of the app.
// It starts the Jetpack Compose interface.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // build the database and the repo
        val db = DataDb.get(applicationContext)
        val repo = Repository(db)
        val settingsRepo = SettingsRepository(applicationContext)

        setContent {
            val appTheme by settingsRepo.theme.collectAsState(initial = AppTheme.PEACH)
            FoodSharingAppTheme(appTheme = appTheme) {
                val homeVm: HomeViewModel = viewModel(factory = ViewModelFactory(repo, settingsRepo))
                AppNav(homeVm = homeVm, settingsRepo = settingsRepo)
            }
        }
    }
}
