package com.example.foodsharingapp.ViewModel

import android.content.Context
import androidx.datastore.core.DataStore
import java.util.prefs.Preferences

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class Settings(private val context: Context) {

    private object Keys {
        val THEME = stringPreferencesKey("theme")
        val CAMERA_ON = booleanPreferencesKey("camera_on")
        val MAP_ON = booleanPreferencesKey("map_on")
    }


}