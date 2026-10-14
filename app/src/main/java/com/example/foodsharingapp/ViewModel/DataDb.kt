package com.example.foodsharingapp.ViewModel

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Place::class, MyFoodRecord::class, ShareRecord::class], version = 1)
abstract class DataDb : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
    abstract fun myFoodRecordDao(): MyFoodRecordDao
    abstract fun shareRecordDao(): ShareRecordDao
}