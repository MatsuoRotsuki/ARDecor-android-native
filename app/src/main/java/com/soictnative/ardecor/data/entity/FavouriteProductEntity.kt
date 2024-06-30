package com.soictnative.ardecor.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_products")
data class FavouriteProductEntity(
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
    val createdAt: Long? = null,
    val userUid: String,
)