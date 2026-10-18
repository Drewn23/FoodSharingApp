package com.example.foodsharingapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Place::class, MyFoodRecord::class, ShareRecord::class],
    version = 2,
    exportSchema = false
)
abstract class DataDb : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
    abstract fun foodDao(): MyFoodRecordDao
    abstract fun shareDao(): ShareRecordDao

    companion object {
        @Volatile private var instance: DataDb? = null

        fun get(context: Context): DataDb =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    DataDb::class.java,
                    "data.db"
                ).fallbackToDestructiveMigration(true).build().also { instance = it }
            }
    }
}
