package com.example.foodsharingapp.data

import androidx.room.Embedded
import androidx.room.Relation

// load a food record and find "Place" whose "id" = "placeId"
// "Place" is nullable, map is optional
// a query result, not a separate database table
data class FoodWithPlace(
    @Embedded val food: MyFoodRecord,
    @Relation(parentColumn = "placeId", entityColumn = "id")
    val place: Place?
)