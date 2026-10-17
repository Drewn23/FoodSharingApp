package com.example.foodsharingapp.data

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow

// save logic in repository

class Repository(private val db: DataDb) {
    private val foodDao = db.foodDao()
    private val placeDao = db.placeDao()
    private val shareDao = db.shareDao()

    val allFood: Flow<List<FoodWithPlace>> = foodDao.getAll()
    val allShares: Flow<List<ShareRecord>> = shareDao.getAll()

    fun getFood(id: Long): Flow<FoodWithPlace?> = foodDao.getById(id)
    fun getSharesForFood(foodId: Long): Flow<List<ShareRecord>> = shareDao.getForFood(foodId)
    fun searchByTag(tag: String): Flow<List<FoodWithPlace>> =
        foodDao.searchByTag(cleanTag(tag))

    suspend fun saveFood(
        photo: String,
        description: String,
        tags: List<String>,
        place: Place?,
        timeDate: Long = System.currentTimeMillis()
    ): Long = db.withTransaction {
        val placeId = place?.let { placeDao.insert(it) }
        val t = cleanTags(tags)
        foodDao.insert(
            MyFoodRecord(
                photo = photo,
                description = description,
                timeDate = timeDate,
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
        newPlace: Place?,
        clearPlace: Boolean = false
    ) = db.withTransaction {
        val placeId = when {
            clearPlace -> null
            newPlace != null -> {
                if (existing.placeId != null) {
                    placeDao.update(newPlace.copy(id = existing.placeId))
                    existing.placeId
                } else {
                    placeDao.insert(newPlace)
                }
            }
            else -> existing.placeId
        }
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
        placeDao.deleteOrphans()
    }

    suspend fun deleteFood(food: MyFoodRecord) = db.withTransaction {
        foodDao.delete(food)
        placeDao.deleteOrphans()
    }
    suspend fun deleteShare(share: ShareRecord) = shareDao.delete(share)
    suspend fun setFavorite(food: MyFoodRecord, favorite: Boolean) =
        foodDao.update(food.copy(isFavorite = favorite))
    suspend fun recordShare(foodId: Long, shareAt: Long = System.currentTimeMillis()): Long =
        shareDao.insert(ShareRecord(foodId = foodId, shareAt = shareAt))

    private fun cleanTag(raw: String): String = raw.trim().removePrefix("@").lowercase()
    private fun cleanTags(raw: List<String>): List<String> =
        raw.map { cleanTag(it) }.filter { it.isNotEmpty() }.distinct().take(3)
}