package com.example.foodsharingapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

enum class AppTheme(val value: String) {
    PURPLE("purple"),
    PEACH("peach"),
    BLUE("blue")
}

class SettingsRepository(private val context: Context) {

    private object Keys {
        val THEME = stringPreferencesKey("theme")
        val CAMERA_ON = booleanPreferencesKey("camera_on")
        val MAP_ON = booleanPreferencesKey("map_on")
    }

    val theme: Flow<AppTheme> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            val rawValue = preferences[Keys.THEME]
            AppTheme.entries.find { it.value == rawValue } ?: AppTheme.PEACH
        }

    val cameraOn: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences -> preferences[Keys.CAMERA_ON] ?: true }

    val mapOn: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences -> preferences[Keys.MAP_ON] ?: true }

    suspend fun setTheme(theme: AppTheme) {
        context.dataStore.edit { preferences ->
            preferences[Keys.THEME] = theme.value
        }
    }

    suspend fun setCameraOn(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.CAMERA_ON] = value
        }
    }

    suspend fun setMapOn(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.MAP_ON] = value
        }
    }
}