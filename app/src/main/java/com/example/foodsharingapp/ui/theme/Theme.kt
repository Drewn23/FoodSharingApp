package com.example.foodsharingapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.example.foodsharingapp.data.AppTheme

// selects the app's Material 3 color scheme based on the selected theme

private val PurpleColorScheme = lightColorScheme(
    primary = PurplePrimary,
    primaryContainer = PurpleContainer,
    onPrimaryContainer = PurpleOnContainer,
    secondary = PurpleSecondary,
    secondaryContainer = PurpleContainer,
    background = PurpleBackground,
    onBackground = PurpleOnBackground,
    surface = PurpleBackground,
    onSurface = PurpleOnBackground,
    surfaceVariant = PurpleContainer,
    onSurfaceVariant = PurpleSecondary,
    outline = PurpleSecondary
)

private val PeachColorScheme = lightColorScheme(
    primary = PeachPrimary,
    primaryContainer = PeachContainer,
    onPrimaryContainer = PeachOnContainer,
    secondary = PeachSecondary,
    secondaryContainer = PeachContainer,
    background = PeachBackground,
    onBackground = PeachOnBackground,
    surface = PeachBackground,
    onSurface = PeachOnBackground,
    surfaceVariant = PeachContainer,
    onSurfaceVariant = PeachSecondary,
    outline = PeachSecondary
)

private val BlueColorScheme = lightColorScheme(
    primary = BluePrimary,
    primaryContainer = BlueContainer,
    onPrimaryContainer = BlueOnContainer,
    secondary = BlueSecondary,
    secondaryContainer = BlueContainer,
    background = BlueBackground,
    onBackground = BlueOnBackground,
    surface = BlueBackground,
    onSurface = BlueOnBackground,
    surfaceVariant = BlueContainer,
    onSurfaceVariant = BlueSecondary,
    outline = BlueSecondary
)

@Composable
fun FoodSharingAppTheme(
    appTheme: AppTheme = AppTheme.PEACH,
    content: @Composable () -> Unit
) {
    val colorScheme = when (appTheme) {
        AppTheme.PURPLE -> PurpleColorScheme
        AppTheme.PEACH -> PeachColorScheme
        AppTheme.BLUE -> BlueColorScheme
    }
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}