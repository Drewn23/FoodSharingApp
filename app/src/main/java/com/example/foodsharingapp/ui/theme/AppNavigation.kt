package com.example.foodsharingapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
fun AppNav() {
    val nav = rememberNavController()

    //val vm: viewModel(add the view model file) = viewModel() "add the class"

    NavHost(navController = nav, startDestination = Login) {
        composable<Login>{
            LoginPage(
                onClickStart = { nav.navigate(Menu)}
            )
        }
        composable<Menu> {
            AppMenu(
                onSettingsClick = { nav.navigate(Settings) },
                onLogoutClick = { nav.navigate(Login) }
            )
        }
        composable<Settings>{
            AppSetting(onCloseClick = { nav.navigate(Menu)})
        }
        composable<AddingState>{

        }
        composable<EditingState>{

        }
    }
}
