package com.soictnative.ardecor.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soictnative.ardecor.data.entity.FavouriteProductEntity
import com.soictnative.ardecor.data.entity.RecentlyViewedProductEntity
import com.soictnative.ardecor.data.entity.ProductImageEntity

@Database(
    entities = [RecentlyViewedProductEntity::class, FavouriteProductEntity::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}