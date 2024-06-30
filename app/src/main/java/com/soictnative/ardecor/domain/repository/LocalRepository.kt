package com.soictnative.ardecor.domain.repository

import com.soictnative.ardecor.data.entity.FavouriteProductEntity
import com.soictnative.ardecor.data.entity.RecentlyViewedProductEntity
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun getRecentlyViewedProductsByUserFromDb(userUid: String): Flow<NetworkResponseState<List<RecentlyViewedProductEntity>>>

    suspend fun getFavouriteProductsByUserFromDb(userUid: String): Flow<NetworkResponseState<List<FavouriteProductEntity>>>

    suspend fun insertRecentlyViewedProductToDb(product: RecentlyViewedProductEntity)

    suspend fun clearRecentlyViewedProductsByUserFromDb(userUid: String)

    suspend fun insertFavouriteProductToDb(product: FavouriteProductEntity)

    suspend fun deleteFavouriteProductFromDb(product: FavouriteProductEntity)
}