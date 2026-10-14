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
    @Query("SELECT * FROM places")
    suspend fun getAllData(): List<Place>
    @Insert suspend fun insert(place: Place): Long
}

@Dao
interface MyFoodRecordDao {
    @Insert suspend fun insert(food: MyFoodRecord)
    @Update suspend fun update(food: MyFoodRecord)
    @Delete suspend fun delete(food: MyFoodRecord)

    @Transaction
    @Query("SELECT * FROM food_data WHERE id = :id")
    fun getById(id: Long): Flow<FoodWithPlace?>

    @Transaction
    @Query(
        """SELECT * FROM food_data
           WHERE tag1 LIKE '%' || :tag || '%'
              OR tag2 LIKE '%' || :tag || '%'
              OR tag3 LIKE '%' || :tag || '%'
           ORDER BY timeDate DESC"""
    )
    fun searchByTag(tag: String): Flow<List<FoodWithPlace>>
}

@Dao
interface ShareRecordDao {
    @Insert suspend fun insert(share: ShareRecord): Long
}
