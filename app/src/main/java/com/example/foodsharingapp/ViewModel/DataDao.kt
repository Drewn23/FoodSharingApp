package com.example.foodsharingapp.ViewModel

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// data access object


@Dao
interface PlaceDao {
    @Query("SELECT * FROM places")
    suspend fun getAllData(): List<Place>
    @Insert suspend fun insert(place: Place)
    @Update suspend fun update(place: Place)
    @Delete suspend fun delete(place: Place)
}

@Dao
interface MyFoodRecordDao {
    @Query("SELECT * FROM food_data")
    suspend fun getAllData(): List<MyFoodRecord>
    @Insert suspend fun insert(food: MyFoodRecord)
    @Update suspend fun update(food: MyFoodRecord)
    @Delete suspend fun delete(food: MyFoodRecord)
}

@Dao
interface ShareRecordDao {
    @Query("SELECT * FROM shares_data")
    suspend fun getAllData(): List<ShareRecord>
    @Insert suspend fun insert(share: ShareRecord)
    @Update suspend fun update(share: ShareRecord)
    @Delete suspend fun delete(share: ShareRecord)
}
