package com.example.foodsharingapp.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
// the entity

// stores location
@Entity(tableName = "places")
data class Place(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val latitude: Double,
    val longitude: Double
)

// My own diary record
@Entity(
    tableName = "food_data",
    foreignKeys = [ForeignKey(
        entity = Place::class,
        parentColumns = ["id"],
        childColumns = ["placeId"],
        onDelete = ForeignKey.SET_NULL
    )],
    indices = [Index("placeId")]
    )
data class MyFoodRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val photo: String,
    val description: String = "",
    val timeDate: Long = System.currentTimeMillis(),
    val tag1: String? = null,
    val tag2: String? = null,
    val tag3: String? = null,
    val placeId: Long? = null,          // null = no map added
    val isFavorite: Boolean = false     // delete later
)

// A share record (when you share an entry through link)
@Entity(
    tableName = "shares_data",
    foreignKeys = [
        ForeignKey(
            entity = MyFoodRecord::class,
            parentColumns = ["id"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("foodId")]
)
data class ShareRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val foodId: Long,
    val shareAt: Long = System.currentTimeMillis()
)