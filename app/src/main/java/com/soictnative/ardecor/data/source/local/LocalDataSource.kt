package com.soictnative.ardecor.data.source.local

import com.soictnative.ardecor.data.entity.FavouriteProductEntity
import com.soictnative.ardecor.data.entity.RecentlyViewedProductEntity

interface LocalDataSource {
    suspend fun getRecentlyViewedProductsByUserFromDb(userUid: String): List<RecentlyViewedProductEntity>

    suspend fun getFavouriteProductsByUserFromDb(userUid: String): List<FavouriteProductEntity>

    suspend fun insertRecentlyViewedProductToDb(product: RecentlyViewedProductEntity)

    suspend fun clearRecentlyViewedProductsByUserFromDb(userUid: String)

    suspend fun insertFavouriteProductToDb(product: FavouriteProductEntity)

    suspend fun deleteFavouriteProductFromDb(product: FavouriteProductEntity)
}