package com.example.foodsharingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodsharingapp.data.DataDb
import com.example.foodsharingapp.ViewModel.HomeViewModel
import com.example.foodsharingapp.data.Repository
import com.example.foodsharingapp.data.TestScreen
import com.example.foodsharingapp.ViewModel.ViewModelFactory
import com.example.foodsharingapp.ui.theme.FoodSharingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // build the database and the repo
        val db = DataDb.get(applicationContext)
        val repo = Repository(db)

        setContent {
            FoodSharingAppTheme {
                // viewmodel, using factory
                val homeVm: HomeViewModel = viewModel(factory = ViewModelFactory(repo))

                // Show the screen
                TestScreen(homeVm)
            }
        }
    }
}
