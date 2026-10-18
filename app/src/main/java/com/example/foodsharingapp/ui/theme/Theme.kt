package com.example.foodsharingapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.foodsharingapp.data.AppTheme

private val RedColorScheme = lightColorScheme(
    primary = RedPrimary,
    primaryContainer = RedContainer,
    onPrimaryContainer = RedOnContainer,
    secondary = RedSecondary,
    secondaryContainer = RedContainer,
    background = RedBackground,
    onBackground = RedOnBackground,
    surface = RedBackground,
    onSurface = RedOnBackground,
    surfaceVariant = RedContainer,
    onSurfaceVariant = RedSecondary,
    outline = RedSecondary
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
        AppTheme.RED -> RedColorScheme
        AppTheme.PEACH -> PeachColorScheme
        AppTheme.BLUE -> BlueColorScheme
    }
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}