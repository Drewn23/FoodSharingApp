package com.example.foodsharingapp.ui.theme

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil3.compose.AsyncImage
import com.example.foodsharingapp.data.Place
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale
import kotlin.coroutines.resume

// provides the form for creating a new food entry
// collects the photo, description, tags and place
// send the completed entry to viewmodel or repository

val CyanButtonColor = Color(0xFF78D6DF)

fun openAppSettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    )
    context.startActivity(intent)
}

fun saveBitmapToCache(context: Context, bitmap: Bitmap): String {
    val file = File(context.cacheDir, "camera_photo_${System.currentTimeMillis()}.jpg")
    val out = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    out.flush()
    out.close()
    return Uri.fromFile(file).toString()
}

suspend fun getDeviceLocation(context: Context): Location? {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    return suspendCancellableCoroutine { continuation ->
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    continuation.resume(location)
                } else {
                    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
                        .addOnSuccessListener { curLoc ->
                            continuation.resume(curLoc)
                        }
                        .addOnFailureListener {
                            continuation.resume(null)
                        }
                }
            }.addOnFailureListener {
                continuation.resume(null)
            }
        } catch (_: SecurityException) {
            continuation.resume(null)
        }
    }
}

suspend fun reverseGeocodeNominatim(lat: Double, lon: Double): String? = withContext(Dispatchers.IO) {
    var connection: HttpURLConnection? = null
    try {
        val urlStr = "https://nominatim.openstreetmap.org/reverse?format=json&lat=$lat&lon=$lon"
        val url = URL(urlStr)
        connection = url.openConnection() as HttpURLConnection
        connection.setRequestProperty("User-Agent", "FoodSharingApp/1.0 (Android)")
        connection.setRequestProperty("Accept-Language", "en")
        connection.connectTimeout = 5000
        connection.readTimeout = 5000

        if (connection.responseCode == 200) {
            val response = connection.inputStream.bufferedReader().use { it.readText() }
            val json = JSONObject(response)
            val displayName = json.optString("display_name", "")
            if (displayName.isNotBlank()) {
                val parts = displayName.split(",")
                return@withContext parts.firstOrNull()?.trim() ?: displayName
            }
        }
    } catch (_: Exception) {
    } finally {
        connection?.disconnect()
    }
    null
}

@Preview(showBackground = true)
@Composable
//Must not be rotatable
fun AddingPage(
    initialPhoto: String = "",
    initialDescription: String = "",
    initialTags: List<String> = emptyList(),
    initialPlace: Place? = null,
    cameraOn: Boolean = true,
    mapOn: Boolean = true,
    onSaveClick: (photo: String, description: String, tags: List<String>, place: Place?) -> Unit = { _, _, _, _ -> },
    onCancelClick: () -> Unit = {}
) {
    //Lock the screen rotation at "portrait" for AddingPage only
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context as? Activity
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    var photo by remember { mutableStateOf(initialPhoto) }
    var description by remember { mutableStateOf(initialDescription) }
    var tag1 by remember { mutableStateOf(initialTags.getOrNull(0) ?: "") }
    var tag2 by remember { mutableStateOf(initialTags.getOrNull(1) ?: "") }
    var tag3 by remember { mutableStateOf(initialTags.getOrNull(2) ?: "") }
    var placeName by remember { mutableStateOf(initialPlace?.name ?: "") }
    var showPhotoDialog by remember { mutableStateOf(false) }
    var showTagDialog by remember { mutableStateOf(false) }
    var showMapDialog by remember { mutableStateOf(false) }
    var showCameraPermissionDeniedDialog by remember { mutableStateOf(false) }
    var showLocationPermissionDeniedDialog by remember { mutableStateOf(false) }

    // Nominatim Network Feedback States
    var isFetchingLocation by remember { mutableStateOf(false) }
    var locationStatusMessage by remember { mutableStateOf<String?>(null) }
    var locationFetchFailed by remember { mutableStateOf(false) }
    var lastCapturedLocation by remember { mutableStateOf<Location?>(null) }
    val scope = rememberCoroutineScope()

    val fetchLocationAction = {
        isFetchingLocation = true
        locationFetchFailed = false
        locationStatusMessage = "Connecting to Nominatim & fetching GPS..."
        scope.launch {
            try {
                val location = getDeviceLocation(context)
                if (location != null) {
                    lastCapturedLocation = location
                    locationStatusMessage = "Querying OpenStreetMap Nominatim..."
                    val place = reverseGeocodeNominatim(location.latitude, location.longitude)
                    if (place != null) {
                        placeName = place
                        locationStatusMessage = "Success! Location retrieved."
                        locationFetchFailed = false
                        showMapDialog = false
                    } else {
                        locationFetchFailed = true
                        locationStatusMessage = "Network request failed (No response)."
                    }
                } else {
                    locationFetchFailed = true
                    locationStatusMessage = "Failed to get GPS location. No signal or location disabled."
                }
            } catch (_: Exception) {
                locationFetchFailed = true
                locationStatusMessage = "Network error / No internet connection. Please retry or use default method."
            } finally {
                isFetchingLocation = false
            }
        }
    }

    val fallbackDefaultGeocoderAction = {
        isFetchingLocation = true
        locationFetchFailed = false
        locationStatusMessage = "Using phone's default method (Geocoder)..."
        scope.launch {
            try {
                val location = lastCapturedLocation ?: getDeviceLocation(context)
                if (location != null) {
                    lastCapturedLocation = location
                    if (Geocoder.isPresent()) {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            geocoder.getFromLocation(location.latitude, location.longitude, 1) { addresses ->
                                val address = addresses.firstOrNull()
                                placeName = address?.locality ?: address?.featureName ?: "Location (${location.latitude.toString().take(6)})"
                                isFetchingLocation = false
                                showMapDialog = false
                            }
                        } else {
                            @Suppress("DEPRECATION")
                            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            val address = addresses?.firstOrNull()
                            placeName = address?.locality ?: address?.featureName ?: "Location (${location.latitude.toString().take(6)})"
                            isFetchingLocation = false
                            showMapDialog = false
                        }
                    } else {
                        // Geocoder not present on this device/emulator
                        placeName = "Location (${location.latitude.toString().take(6)}, ${location.longitude.toString().take(6)})"
                        isFetchingLocation = false
                        showMapDialog = false
                    }
                } else {
                    isFetchingLocation = false
                    locationFetchFailed = true
                    locationStatusMessage = "Unable to get device location for default method."
                }
            } catch (_: Exception) {
                isFetchingLocation = false
                locationFetchFailed = true
                locationStatusMessage = "Default geocoder method failed."
            }
        }
    }

    // Launchers for gallery, camera, and location
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {}
            photo = it.toString()
            showPhotoDialog = false
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            val uriStr = saveBitmapToCache(context, it)
            photo = uriStr
            showPhotoDialog = false
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            showCameraPermissionDeniedDialog = true
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            fetchLocationAction()
        } else {
            showLocationPermissionDeniedDialog = true
        }
    }

    val parsedTags = remember(tag1, tag2, tag3) {
        listOf(tag1, tag2, tag3).map { it.trim() }.filter { it.isNotEmpty() }
    }

    val currentPlace = remember(placeName) {
        if (placeName.isNotBlank()) Place(name = placeName, latitude = 0.0, longitude = 0.0) else null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Right Save Button
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = "Save",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = if (photo.isNotBlank()) Color.Black else Color.Gray,
                modifier = Modifier
                    .clickable(enabled = photo.isNotBlank()) {
                        onSaveClick(photo, description, parsedTags, currentPlace)
                    }
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Large Box: Click to save a picture
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(CyanButtonColor)
                .clickable { showPhotoDialog = true },
            contentAlignment = Alignment.Center
        ) {
            if (photo.isBlank()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Click to save a picture",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                }
            } else {
                AsyncImage(
                    model = photo,
                    contentDescription = "Selected Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Middle Row: Add tag & Add map buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Add tag button, take 0.5f empty space
            Box(
                modifier = Modifier
                    .size(width = 110.dp, height = 120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(CyanButtonColor)
                    .clickable { showTagDialog = true }
                    .weight(0.5f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (parsedTags.isEmpty()) "Add tag" else "Tags:\n${parsedTags.joinToString(", ")}",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    modifier = Modifier.padding(4.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            // Add map button must take up 0.5f of space
            Box(
                modifier = Modifier
                    .size(width = 110.dp, height = 120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(CyanButtonColor)
                    .clickable {
                        if (!mapOn) {
                            Toast.makeText(context, "Map/Location feature is disabled in Settings", Toast.LENGTH_SHORT).show()
                        } else {
                            showMapDialog = true
                        }
                    }
                    .weight(0.5f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (placeName.isBlank()) "Add\nmap" else "Location:\n$placeName",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Description Box must display "Description" as text before when entering Show what is entered instead
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = {
                Text(
                    text = "Put a Description!!",
                    fontSize = 18.sp,
                    color = Color.Black.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            textStyle = TextStyle(
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                color = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(CyanButtonColor),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        // Cancel Button at bottom
        Button(
            onClick = onCancelClick,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CyanButtonColor,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .width(180.dp)
                .height(50.dp)
        ) {
            Text(
                text = "Cancel",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    // Dialogs for user input, camera and map
    if (showPhotoDialog) {
        var tempPhoto by remember { mutableStateOf(photo) }
        AlertDialog(
            onDismissRequest = { showPhotoDialog = false },
            title = { Text("Insert Picture") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanButtonColor, contentColor = Color.Black)
                    ) {
                        Text("Pick from Gallery")
                    }

                    Button(
                        onClick = {
                            if (!cameraOn) {
                                Toast.makeText(context, "Camera feature is disabled in Settings", Toast.LENGTH_SHORT).show()
                            } else {
                                val hasCameraPermission = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED

                                if (hasCameraPermission) {
                                    cameraLauncher.launch(null)
                                } else {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanButtonColor, contentColor = Color.Black)
                    ) {
                        Text("Take Photo (Camera)")
                    }

                    OutlinedTextField(
                        value = tempPhoto,
                        onValueChange = { tempPhoto = it },
                        placeholder = { Text("Or enter photo URI") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (tempPhoto.isNotBlank()) {
                        photo = tempPhoto
                    }
                    showPhotoDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPhotoDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showCameraPermissionDeniedDialog) {
        AlertDialog(
            onDismissRequest = { showCameraPermissionDeniedDialog = false },
            title = { Text("Camera Permission Required") },
            text = {
                Text("Camera permission was denied. Android prevents showing the prompt again. Please enable Camera permission in App Settings, or select a photo from Gallery.")
            },
            confirmButton = {
                TextButton(onClick = {
                    showCameraPermissionDeniedDialog = false
                    openAppSettings(context)
                }) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCameraPermissionDeniedDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showTagDialog) {
        var tempTag1 by remember { mutableStateOf(tag1) }
        var tempTag2 by remember { mutableStateOf(tag2) }
        var tempTag3 by remember { mutableStateOf(tag3) }

        AlertDialog(
            onDismissRequest = { showTagDialog = false },
            title = { Text("Add Tags (Up to 3)") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = tempTag1,
                        onValueChange = { tempTag1 = it },
                        placeholder = { Text("Tag 1 (e.g. Breakfast)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = tempTag2,
                        onValueChange = { tempTag2 = it },
                        placeholder = { Text("Tag 2 (e.g. Yum)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = tempTag3,
                        onValueChange = { tempTag3 = it },
                        placeholder = { Text("Tag 3 (e.g. Healthy)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    tag1 = tempTag1
                    tag2 = tempTag2
                    tag3 = tempTag3
                    showTagDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTagDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showMapDialog) {
        var tempPlace by remember { mutableStateOf(placeName) }
        AlertDialog(
            onDismissRequest = {
                if (!isFetchingLocation) {
                    showMapDialog = false
                }
            },
            title = { Text("Add Map Location") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isFetchingLocation) {
                        CircularProgressIndicator(color = Color.Black)
                        Text(
                            text = locationStatusMessage ?: "Loading location...",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp
                        )
                    } else {
                        Button(
                            onClick = {
                                val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

                                if (hasFine || hasCoarse) {
                                    fetchLocationAction()
                                } else {
                                    locationPermissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = CyanButtonColor, contentColor = Color.Black)
                        ) {
                            Text("Use Device Location")
                        }

                        if (locationFetchFailed) {
                            Text(
                                text = locationStatusMessage ?: "Nominatim network request failed.",
                                color = Color.Red,
                                textAlign = TextAlign.Center,
                                fontSize = 13.sp
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { fetchLocationAction() },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800), contentColor = Color.White)
                                ) {
                                    Text("Retry")
                                }
                                Button(
                                    onClick = { fallbackDefaultGeocoderAction() },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50), contentColor = Color.White)
                                ) {
                                    Text("Use Default Method")
                                }
                            }
                        }

                        OutlinedTextField(
                            value = tempPlace,
                            onValueChange = { tempPlace = it },
                            placeholder = { Text("Or enter location name manually") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (tempPlace.isNotBlank()) {
                            Button(
                                onClick = {
                                    tempPlace = ""
                                    placeName = ""
                                    locationStatusMessage = null
                                    locationFetchFailed = false
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B), contentColor = Color.White)
                            ) {
                                Text("Clear Location")
                            }
                        }
                    }
                }
            },
            confirmButton = {
                if (!isFetchingLocation) {
                    TextButton(onClick = {
                        placeName = tempPlace
                        showMapDialog = false
                    }) {
                        Text("OK")
                    }
                }
            },
            dismissButton = {
                if (!isFetchingLocation) {
                    TextButton(onClick = { showMapDialog = false }) {
                        Text("Cancel")
                    }
                }
            }
        )
    }

    if (showLocationPermissionDeniedDialog) {
        AlertDialog(
            onDismissRequest = { showLocationPermissionDeniedDialog = false },
            title = { Text("Location Permission Required") },
            text = {
                Text("Location permission was denied. Android prevents showing the prompt again. Please enable Location permission in App Settings, or enter a location manually.")
            },
            confirmButton = {
                TextButton(onClick = {
                    showLocationPermissionDeniedDialog = false
                    openAppSettings(context)
                }) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLocationPermissionDeniedDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
