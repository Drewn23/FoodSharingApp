package com.example.foodsharingapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.foodsharingapp.ViewModel.HomeViewModel
import com.example.foodsharingapp.ViewModel.ViewModelFactory
import com.example.foodsharingapp.data.AppTheme
import com.example.foodsharingapp.data.DataDb
import com.example.foodsharingapp.data.Place
import com.example.foodsharingapp.data.Repository
import com.example.foodsharingapp.data.SettingsRepository
import com.example.foodsharingapp.ui.theme.AppNav
import com.example.foodsharingapp.ui.theme.FoodSharingAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        handleIncomingShareIntent(intent, repo)

        setContent {
            val appTheme by settingsRepo.theme.collectAsState(initial = AppTheme.PEACH)
            FoodSharingAppTheme(appTheme = appTheme) {
                val homeVm: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = ViewModelFactory(repo, settingsRepo))
                AppNav(homeVm = homeVm, settingsRepo = settingsRepo)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val db = DataDb.get(applicationContext)
        val repo = Repository(db)
        handleIncomingShareIntent(intent, repo)
    }

    private fun handleIncomingShareIntent(intent: Intent?, repo: Repository) {
        val data: Uri? = intent?.data
        if (data != null && data.path?.contains("share") == true) {
            val desc = data.getQueryParameter("desc") ?: ""
            val photo = data.getQueryParameter("photo") ?: ""
            val tag1 = data.getQueryParameter("tag1")
            val tag2 = data.getQueryParameter("tag2")
            val tag3 = data.getQueryParameter("tag3")
            val placeName = data.getQueryParameter("place") ?: ""

            if (desc.isNotBlank() || photo.isNotBlank()) {
                val tags = listOfNotNull(tag1, tag2, tag3).filter { it.isNotBlank() }
                val place = if (placeName.isNotBlank()) Place(name = placeName, latitude = 0.0, longitude = 0.0) else null

                CoroutineScope(Dispatchers.IO).launch {
                    repo.saveFood(photo, desc, tags, place)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "Shared entry saved to your database!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
