package com.example.foodsharingapp.ViewModel

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Place::class, MyFoodRecord::class, ShareRecord::class], version = 1)
abstract class DataDb : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
    abstract fun myFoodRecordDao(): MyFoodRecordDao
    abstract fun shareRecordDao(): ShareRecordDao

    companion object {
        @Volatile private var instance: DataDb? = null

        fun get(context: Context): DataDb =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    DataDb::class.java,
                    "data.db"
                ).build().also { instance = it }
            }
    }
}