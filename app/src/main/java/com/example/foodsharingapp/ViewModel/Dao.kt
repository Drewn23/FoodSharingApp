package com.example.foodsharingapp.ViewModel

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    //using suspend so function runs on background thread, not ui thread
    @Insert
    suspend fun insertEntry(entry: DataEntry): Long

    @Update
    suspend fun updateEntry(entry: DataEntry)

    @Delete
    suspend fun deleteEntry(entry: DataEntry)

    @Query("SELECT * FROM diary_entries ORDER BY createdAt DESC")
    fun getAllEntries(): Flow<List<DataEntry>>

    @Query("SELECT * FROM diary_entries WHERE id = :id")
    suspend fun getEntryById(id: Long): DataEntry?

    @Query("SELECT * FROM diary_entries WHERE ownerId = :ownerId")
    fun getEntriesByOwner(ownerId: Long): Flow<List<DataEntry>>
}

annotation class DataEntry
