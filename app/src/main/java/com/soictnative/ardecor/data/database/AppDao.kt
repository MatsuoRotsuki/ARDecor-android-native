package com.soictnative.ardecor.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.soictnative.ardecor.data.entity.FavouriteProductEntity
import com.soictnative.ardecor.data.entity.RecentlyViewedProductEntity

@Dao
interface AppDao {

    @Insert(RecentlyViewedProductEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentlyViewedProduct(product: RecentlyViewedProductEntity)

    @Query("SELECT * FROM recently_viewed_products WHERE userUid = :userUid ORDER BY lastAccessedTime DESC")
    suspend fun getRecentlyViewedProductsByUserUid(userUid: String): List<RecentlyViewedProductEntity>

    @Query("DELETE FROM recently_viewed_products WHERE userUid = :userUid")
    suspend fun clearRecentlyViewedProductsByUserUid(userUid: String)

    @Insert(FavouriteProductEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteProduct(product: FavouriteProductEntity)

    @Query("SELECT * FROM favourite_products WHERE userUid = :userUid ORDER BY createdAt DESC")
    suspend fun getFavouriteProductsByUserUid(userUid: String): List<FavouriteProductEntity>

    @Delete(FavouriteProductEntity::class)
    suspend fun deleteFavoriteProduct(favoriteProductEntity: FavouriteProductEntity)
}