package com.soictnative.ardecor.data.source.local

import com.soictnative.ardecor.data.database.AppDao
import com.soictnative.ardecor.data.entity.FavouriteProductEntity
import com.soictnative.ardecor.data.entity.RecentlyViewedProductEntity
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val appDao: AppDao
) : LocalDataSource {
    override suspend fun getRecentlyViewedProductsByUserFromDb(userUid: String): List<RecentlyViewedProductEntity> {
        return appDao.getRecentlyViewedProductsByUserUid(userUid)
    }

    override suspend fun getFavouriteProductsByUserFromDb(userUid: String): List<FavouriteProductEntity> {
        return appDao.getFavouriteProductsByUserUid(userUid)
    }

    override suspend fun insertRecentlyViewedProductToDb(product: RecentlyViewedProductEntity) {
        appDao.insertRecentlyViewedProduct(product)
    }

    override suspend fun clearRecentlyViewedProductsByUserFromDb(userUid: String) {
        appDao.clearRecentlyViewedProductsByUserUid(userUid)
    }

    override suspend fun insertFavouriteProductToDb(product: FavouriteProductEntity) {
        appDao.insertFavouriteProduct(product)
    }

    override suspend fun deleteFavouriteProductFromDb(product: FavouriteProductEntity) {
        appDao.deleteFavoriteProduct(product)
    }
}