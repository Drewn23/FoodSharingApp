package com.example.foodsharingapp.ViewModel

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// data access object


@Dao
interface PlaceDao {
    @Query("DELETE FROM places WHERE id NOT IN (SELECT placeId FROM food_data WHERE placeId IS NOT NULL)")
    suspend fun deleteOrphans()

    @Query("SELECT * FROM places")
    suspend fun getAllData(): List<Place>
    @Insert suspend fun insert(place: Place): Long
}

@Dao
interface MyFoodRecordDao {
    @Insert suspend fun insert(food: MyFoodRecord): Long
    @Update suspend fun update(food: MyFoodRecord)
    @Delete suspend fun delete(food: MyFoodRecord)

    @Transaction
    @Query("SELECT * FROM food_data ORDER BY timeDate DESC")
    fun getAll(): Flow<List<FoodWithPlace>>

    @Transaction
    @Query("SELECT * FROM food_data WHERE id = :id")
    fun getById(id: Long): Flow<FoodWithPlace?>

    @Transaction
    @Query(
        """SELECT * FROM food_data
       WHERE tag1 = :tag OR tag2 = :tag OR tag3 = :tag
       ORDER BY timeDate DESC"""
    )
    fun searchByTag(tag: String): Flow<List<FoodWithPlace>>
}

@Dao
interface ShareRecordDao {
    @Insert suspend fun insert(share: ShareRecord): Long
    @Delete suspend fun delete(share: ShareRecord)
}
