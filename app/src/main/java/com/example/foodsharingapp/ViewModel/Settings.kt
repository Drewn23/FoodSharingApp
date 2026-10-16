package com.example.foodsharingapp.ViewModel

import android.content.Context
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.prefs.Preferences

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class Settings(private val context: Context) {

    private object Keys {
        val THEME = stringPreferencesKey("theme")
        val CAMERA_ON = booleanPreferencesKey("camera_on")
        val MAP_ON = booleanPreferencesKey("map_on")
    }

    val theme: Flow<String> = context.dataStore.data.map { it[Keys.THEME] ?: "tan" }
    val cameraOn: Flow<Boolean> = context.dataStore.data.map { it[Keys.CAMERA_ON] ?: true }
    val mapOn: Flow<Boolean> = context.dataStore.data.map { it[Keys.MAP_ON] ?: true }

    suspend fun setTheme(value: String) = context.dataStore.edit { it[Keys.THEME] = value }
    suspend fun setCameraOn(value: Boolean) = context.dataStore.edit { it[Keys.CAMERA_ON] = value }
    suspend fun setMapOn(value: Boolean) = context.dataStore.edit { it[Keys.MAP_ON] = value }

}