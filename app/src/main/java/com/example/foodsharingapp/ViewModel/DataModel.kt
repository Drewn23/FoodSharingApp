package com.example.foodsharingapp.ViewModel

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey

// My own diary record
@Entity(tableName = "food_data")
data class MyFoodRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val photo: String,
    val location: Double,
    val description: String,
    val timeDate: String,
    val tag1: String?,
    val tag2: String?,
    val tag3: String?,
    val ownerId: Long,
    val isFavorite: Boolean = false
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
    val shareToken: String,
    val shareAt: Double,
    val viewCount: Int = 0,
)

// A saved friend's entry (when you save someone else's diary)
@Entity(
    tableName = "saved_data",
    foreignKeys = [
        ForeignKey(
            entity = MyFoodRecord::class,
            parentColumns = ["id"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
data class SavedFriendData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val foodId: Long,
    val userId: Long,
    val savedAt: Double
)