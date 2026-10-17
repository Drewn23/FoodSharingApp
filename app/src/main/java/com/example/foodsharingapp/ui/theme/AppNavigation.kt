package com.example.foodsharingapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodsharingapp.ViewModel.HomeViewModel
import com.example.foodsharingapp.data.SettingsRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Menu

@Serializable
object Settings

@Serializable
object AddingState

@Serializable
object EditingState

@Composable
fun AppNav(
    homeVm: HomeViewModel,
    settingsRepo: SettingsRepository
) {
    val nav = rememberNavController()
    val scope = rememberCoroutineScope()

    val foods by homeVm.foods.collectAsState()
    val searchQuery by homeVm.searchText.collectAsState()

    val cameraOn by settingsRepo.cameraOn.collectAsState(initial = true)
    val mapOn by settingsRepo.mapOn.collectAsState(initial = true)

    NavHost(navController = nav, startDestination = Login) {
        composable<Login>{
            LoginPage(
                onClickStart = { nav.navigate(Menu)}
            )
        }
        composable<Menu> {
            AppMenu(
                foods = foods,
                searchQuery = searchQuery,
                onSearchChange = homeVm::onSearchChange,
                onSettingsClick = { nav.navigate(Settings) },
                onLogoutClick = { nav.navigate(Login) }
            )
        }
        composable<Settings>{
            AppSetting(
                onCloseClick = { nav.navigate(Menu)},
                cameraOn = cameraOn,
                mapOn = mapOn,
                onCameraToggle = { enabled -> scope.launch { settingsRepo.setCameraOn(enabled) } },
                onMapToggle = { enabled -> scope.launch { settingsRepo.setMapOn(enabled) } }
            )
        }
        composable<AddingState>{

        }
        composable<EditingState>{

        }
    }
}
