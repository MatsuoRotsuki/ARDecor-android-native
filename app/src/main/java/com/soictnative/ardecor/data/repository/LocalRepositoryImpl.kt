package com.soictnative.ardecor.data.repository

import com.soictnative.ardecor.data.entity.FavouriteProductEntity
import com.soictnative.ardecor.data.entity.RecentlyViewedProductEntity
import com.soictnative.ardecor.data.source.local.LocalDataSource
import com.soictnative.ardecor.di.IoDispatcher
import com.soictnative.ardecor.domain.repository.LocalRepository
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val localDataSource: LocalDataSource
) : LocalRepository {
    override suspend fun getRecentlyViewedProductsByUserFromDb(userUid: String): Flow<NetworkResponseState<List<RecentlyViewedProductEntity>>> {
        return flow {
            emit(NetworkResponseState.Success(localDataSource.getRecentlyViewedProductsByUserFromDb(userUid)))
        }.flowOn(ioDispatcher)
    }

    override suspend fun getFavouriteProductsByUserFromDb(userUid: String): Flow<NetworkResponseState<List<FavouriteProductEntity>>> {
        return flow {
            emit(NetworkResponseState.Success(localDataSource.getFavouriteProductsByUserFromDb(userUid)))
        }.flowOn(ioDispatcher)
    }

    override suspend fun insertRecentlyViewedProductToDb(product: RecentlyViewedProductEntity) {
        withContext(ioDispatcher) {
            localDataSource.insertRecentlyViewedProductToDb(product)
        }
    }

    override suspend fun clearRecentlyViewedProductsByUserFromDb(userUid: String) {
        withContext(ioDispatcher) {
            localDataSource.clearRecentlyViewedProductsByUserFromDb(userUid)
        }
    }

    override suspend fun insertFavouriteProductToDb(product: FavouriteProductEntity) {
        withContext(ioDispatcher) {
            localDataSource.insertFavouriteProductToDb(product)
        }
    }

    override suspend fun deleteFavouriteProductFromDb(product: FavouriteProductEntity) {
        withContext(ioDispatcher) {
            localDataSource.deleteFavouriteProductFromDb(product)
        }
    }
}