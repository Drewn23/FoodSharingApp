package com.example.foodsharingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodsharingapp.ViewModel.DataDb
import com.example.foodsharingapp.ViewModel.HomeViewModel
import com.example.foodsharingapp.ViewModel.Repository
import com.example.foodsharingapp.ViewModel.TestScreen
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
