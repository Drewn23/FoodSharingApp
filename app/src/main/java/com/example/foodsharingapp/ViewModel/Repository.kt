package com.example.foodsharingapp.ViewModel

import androidx.core.app.OnNewIntentProvider
import androidx.datastore.core.DataStore
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.prefs.Preferences

// save logic in repository

class Repository(private val db: DataDb) {
    private val foodDao = db.foodDao()
    private val placeDao = db.placeDao()
    private val shareDao = db.shareDao()

    val allFood: Flow<List<FoodWithPlace>> = foodDao.getAll()
    fun getFood(id: Long): Flow<FoodWithPlace?> = foodDao.getById(id)
    fun searchFood(tag: String): Flow<List<FoodWithPlace>> = foodDao.searchByTag(cleanTag(tag))

    suspend fun saveFood(
        photo: String,
        description: String,
        tags: List<String>,
        place: Place?
    ): Long = db.withTransaction {
        val placeId = place?.let { placeDao.insert(it) }
        val t = cleanTags(tags)
        foodDao.insert(
            MyFoodRecord(
                photo = photo,
                description = description,
                tag1 = t.getOrNull(0),
                tag2 = t.getOrNull(1),
                tag3 = t.getOrNull(2),
                placeId = placeId
            )
        )
    }

    suspend fun updateFood(
        existing: MyFoodRecord,
        photo: String,
        description: String,
        tags: List<String>,
        newplace: Place?
    ) = db.withTransaction {
        val placeId = newplace?.let { placeDao.insert(it) } ?: existing.placeId
        val t = cleanTags(tags)
        foodDao.update(
            existing.copy(
                photo = photo,
                description = description,
                tag1 = t.getOrNull(0),
                tag2 = t.getOrNull(1),
                tag3 = t.getOrNull(2),
                placeId = placeId
            )
        )
    }

    suspend fun deleteFood(food: MyFoodRecord) = foodDao.delete(food)
    suspend fun deleteShare(share: ShareRecord) = shareDao.delete(share)
    suspend fun setFavorite(food: MyFoodRecord, favorite: Boolean) =
        foodDao.update(food.copy(isFavorite = favorite))
    suspend fun recordShare(foodId: Long, shareAt: Double) =
        shareDao.insert(ShareRecord(foodId = foodId))

    private fun cleanTag(raw: String): String = raw.trim().removePrefix("@").lowercase()
    private fun cleanTags(raw: List<String>): List<String> =
        raw.map { cleanTag(it) }.filter { it.isNotEmpty() }.distinct().take(3)
}