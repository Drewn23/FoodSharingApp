package com.example.foodsharingapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.foodsharingapp.ViewModel.HomeViewModel
import com.example.foodsharingapp.data.FoodWithPlace
import com.example.foodsharingapp.data.Place
import com.example.foodsharingapp.data.SettingsRepository
import kotlinx.coroutines.launch
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
data class EditingState(
    val foodId: Long,
    val photo: String,
    val description: String,
    val tag1: String = "",
    val tag2: String = "",
    val tag3: String = "",
    val placeName: String = ""
)

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
        composable<Login> {
            LoginPage(
                onClickStart = { nav.navigate(Menu) }
            )
        }
        composable<Menu> {
            AppMenu(
                foods = foods,
                searchQuery = searchQuery,
                onSearchChange = homeVm::onSearchChange,
                onSettingsClick = { nav.navigate(Settings) },
                onLogoutClick = { nav.navigate(Login) },
                onAddClick = { nav.navigate(AddingState) },
                onItemClick = { item ->
                    nav.navigate(
                        EditingState(
                            foodId = item.food.id,
                            photo = item.food.photo,
                            description = item.food.description,
                            tag1 = item.food.tag1 ?: "",
                            tag2 = item.food.tag2 ?: "",
                            tag3 = item.food.tag3 ?: "",
                            placeName = item.place?.name ?: ""
                        )
                    )
                }
            )
        }
        composable<Settings> {
            AppSetting(
                onCloseClick = { nav.navigate(Menu) },
                cameraOn = cameraOn,
                mapOn = mapOn,
                onCameraToggle = { enabled -> scope.launch { settingsRepo.setCameraOn(enabled) } },
                onMapToggle = { enabled -> scope.launch { settingsRepo.setMapOn(enabled) } }
            )
        }
        composable<AddingState> {
            AddingPage(
                onSaveClick = { photo, description, tags, place ->
                    homeVm.saveFood(photo, description, tags, place)
                    nav.navigate(Menu)
                },
                onCancelClick = { nav.navigate(Menu) }
            )
        }
        composable<EditingState> { backStackEntry ->
            val args = backStackEntry.toRoute<EditingState>()
            val initialTags = listOfNotNull(args.tag1, args.tag2, args.tag3).filter { it.isNotBlank() }
            val initialPlace = if (args.placeName.isNotBlank()) Place(name = args.placeName, latitude = 0.0, longitude = 0.0) else null

            val existingItem = foods.find { it.food.id == args.foodId }

            EditingPage(
                initialPhoto = args.photo,
                initialDescription = args.description,
                initialTags = initialTags,
                initialPlace = initialPlace,
                onSaveClick = { photo, description, tags, place ->
                    if (existingItem != null) {
                        homeVm.updateFood(existingItem.food, photo, description, tags, place)
                    }
                    nav.navigate(Menu)
                },
                onDeleteClick = {
                    if (existingItem != null) {
                        homeVm.delete(existingItem)
                    }
                    nav.navigate(Menu)
                },
                onCancelClick = { nav.navigate(Menu) }
            )
        }
    }
}
