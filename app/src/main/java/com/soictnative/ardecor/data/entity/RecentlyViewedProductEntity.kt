package com.soictnative.ardecor.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recently_viewed_products")
data class RecentlyViewedProductEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val price: Float,
    val modelPath: String,
    val isStackable: Boolean,
    val source: String?,
    val description: String?,
    val imageUrl: String,
    val categoryId: Int,
    val lastAccessedTime: Long? = null,
    val userUid: String,
)