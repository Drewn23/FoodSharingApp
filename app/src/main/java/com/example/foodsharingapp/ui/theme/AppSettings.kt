package com.example.foodsharingapp.ui.theme

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.foodsharingapp.ViewModel.SettingsViewModel
import com.example.foodsharingapp.data.AppTheme

// Connects the screen to its ViewModel. The only place that touches the ViewModel.
@Composable
fun SettingsScreen(vm: SettingsViewModel, onBack: () -> Unit) {
    val theme by vm.theme.collectAsState()
    val cameraOn by vm.cameraOn.collectAsState()
    val mapOn by vm.mapOn.collectAsState()

    AppSetting(
        selectedTheme = theme,
        cameraOn = cameraOn,
        mapOn = mapOn,
        onBack = onBack,
        onThemeChange = vm::setTheme,
        onCameraToggle = vm::setCameraOn,
        onMapToggle = vm::setMapOn
    )
}

// Stateless: displays what it is given and reports events.
@Composable
fun AppSetting(
    selectedTheme: AppTheme,
    cameraOn: Boolean,
    mapOn: Boolean,
    onBack: () -> Unit,
    onThemeChange: (AppTheme) -> Unit,
    onCameraToggle: (Boolean) -> Unit,
    onMapToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    version: String = "Version 1.0.0"
) {
    Column(modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        SettingHeader(onBack)

        // Scrolls in landscape and at large text sizes
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ThemeSetting(selectedTheme, onThemeChange)
            SettingToggle("Camera", cameraOn, onCameraToggle)
            SettingToggle("Map", mapOn, onMapToggle)
        }

        Text(
            text = version,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .navigationBarsPadding()
                .padding(16.dp)
        )
    }
}

@Composable
private fun SettingHeader(onBack: () -> Unit) {
    Surface(color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .heightIn(min = 56.dp)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
            }
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.size(48.dp)) // balances the back button so the title stays centred
        }
    }
}

@Composable
fun ThemeSetting(selected: AppTheme, onSelect: (AppTheme) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Theme", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ThemeSwatch(AppTheme.RED, RedPrimary, "Red", selected, onSelect)
            ThemeSwatch(AppTheme.PEACH, PeachContainer, "Peach", selected, onSelect)
            ThemeSwatch(AppTheme.BLUE, BluePrimary, "Blue", selected, onSelect)
        }
    }
}

@Composable
private fun ThemeSwatch(
    theme: AppTheme,
    color: Color,
    name: String,
    selected: AppTheme,
    onSelect: (AppTheme) -> Unit
) {
    val isSelected = theme == selected
    val shape = RoundedCornerShape(12.dp)
    Box(
        Modifier
            .size(56.dp)
            .clip(shape)
            .background(color)
            .border(
                width = if (isSelected) 4.dp else 1.dp,   // selection shown by more than color
                color = MaterialTheme.colorScheme.onBackground,
                shape = shape
            )
            .selectable(selected = isSelected, role = Role.RadioButton, onClick = { onSelect(theme) })
            .semantics { contentDescription = "$name theme" }
    )
}

// Label + sliding ON/OFF switch. The whole column is one toggle, so a screen reader
// announces "Camera, switch, on" and the touch target is large.
@Composable
private fun SettingToggle(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val context = LocalContext.current
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onCheckedChange(true)
        } else {
            Toast.makeText(context, "Camera permission denied.", Toast.LENGTH_SHORT).show()
            onCheckedChange(false)
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            onCheckedChange(true)
        } else {
            Toast.makeText(context, "Location permission denied.", Toast.LENGTH_SHORT).show()
            onCheckedChange(false)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .toggleable(
                value = checked,
                role = Role.Switch,
                onValueChange = { newValue ->
                    if (newValue) {
                        if (label.equals("Camera", ignoreCase = true)) {
                            val hasCam = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                            if (hasCam) {
                                onCheckedChange(true)
                            } else {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        } else if (label.equals("Map", ignoreCase = true)) {
                            val hasFine = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                            val hasCoarse = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                            if (hasFine || hasCoarse) {
                                onCheckedChange(true)
                            } else {
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            }
                        } else {
                            onCheckedChange(true)
                        }
                    } else {
                        onCheckedChange(false)
                    }
                }
            )
            .padding(8.dp)
    ) {
        Text(label, style = MaterialTheme.typography.titleMedium)
        SlideSwitchVisual(checked)
    }
}

// Purely visual: the tap is handled by SettingToggle above.
@Composable
private fun SlideSwitchVisual(checked: Boolean) {
    val thumb = @Composable {
        Box(
            Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
    }
    val stateText = @Composable {
        Text(
            text = if (checked) "ON" else "OFF",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
    Row(
        modifier = Modifier
            .defaultMinSize(minWidth = 104.dp, minHeight = 48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (checked) {
            stateText()
            thumb()
        } else {
            thumb()
            stateText()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppSettingPreview() {
    FoodSharingAppTheme {
        AppSetting(
            selectedTheme = AppTheme.PEACH,
            cameraOn = true,
            mapOn = false,
            onBack = {},
            onThemeChange = {},
            onCameraToggle = {},
            onMapToggle = {}
        )
    }
}
