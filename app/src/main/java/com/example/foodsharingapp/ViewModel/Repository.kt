package com.example.foodsharingapp.ViewModel

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.prefs.Preferences

// save logic in repository

class Repository(
    private val diaryDao: Dao,
    private val geocodingApi: GeocodingApi,
    private val dataStore: DataStore<Preferences>
) {

    // Room
    suspend fun insertEntry(entry: AddEntry): Long =
        diaryDao.insertEntry(entry)

    suspend fun updateEntry(entry: AddEntry) =
        diaryDao.updateEntry(entry)

    suspend fun deleteEntry(entry: AddEntry) =
        diaryDao.deleteEntry(entry)

    fun getAllEntries(): Flow<List<AddEntry>> =
        diaryDao.getAllEntries()

    suspend fun getEntryById(id: Long): AddEntry? =
        diaryDao.getEntryById(id)

    // Network
    suspend fun reverseGeocode(lat: Double, lon: Double): String? =
        try {
            val response = geocodingApi.reverse(lat, lon)
            response.displayName
        } catch (e: Exception) {
            null   // bad-data handling: return null instead of crashing
        }

    // DataStore
    val userName: Flow<String>
        get() = dataStore.data
            .map {
                val USER_NAME = null
                it[USER_NAME] ?: "Anonymous"
            }

    suspend fun setUserName(name: String) {
        val edit = dataStore.edit { it[USER_NAME] = name }
    }
}

annotation class GeocodingApi
