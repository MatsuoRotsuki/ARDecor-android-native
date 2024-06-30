package com.soictnative.ardecor.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "product_images", foreignKeys = arrayOf(
    ForeignKey(
        entity = RecentlyViewedProductEntity::class,
        parentColumns = ["id"],
        childColumns = ["product_id"],
        onDelete = ForeignKey.CASCADE
    )
))
data class ProductImageEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    @ColumnInfo(name = "product_id", index = true) val productId: Long,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
)