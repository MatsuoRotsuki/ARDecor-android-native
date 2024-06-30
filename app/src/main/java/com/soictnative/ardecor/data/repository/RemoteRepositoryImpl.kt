package com.soictnative.ardecor.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.soictnative.ardecor.data.api.ApiService
import com.soictnative.ardecor.data.dto.Category
import com.soictnative.ardecor.data.dto.Idea
import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.data.dto.ResponseObject
import com.soictnative.ardecor.data.dto.RoomType
import com.soictnative.ardecor.data.dto.SavedPlacement
import com.soictnative.ardecor.data.source.HomePagingSource
import com.soictnative.ardecor.data.source.remote.RemoteDataSource
import com.soictnative.ardecor.di.IoDispatcher
import com.soictnative.ardecor.domain.repository.RemoteRepository
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    //
    private val apiService: ApiService
) : RemoteRepository {
    override fun getAllProducts(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(pageSize = 24),
            pagingSourceFactory = {
                HomePagingSource(apiService = apiService)
            }
        ).flow
    }

    override fun getAllProductsFromApi(): Flow<NetworkResponseState<ResponseObject<List<Product>>>> {
        return remoteDataSource.getAllProductsFromApi().map {
            when(it) {
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Success -> NetworkResponseState.Success(it.result)
                is NetworkResponseState.Error -> NetworkResponseState.Error(it.exception)
            }
        }.flowOn(ioDispatcher)
    }

    override fun searchProductsFromApi(
        keyword: String?,
        categoryId: Int?,
        sortBy: String?,
        minPrice: Float?,
        maxPrice: Float?,
    ): Flow<NetworkResponseState<ResponseObject<List<Product>>>> {
        return remoteDataSource.searchProductsFromApi(
            keyword = keyword,
            categoryId = categoryId,
            sortBy = sortBy,
            minPrice = minPrice,
            maxPrice = maxPrice,
        ).map {
            when(it) {
               is NetworkResponseState.Loading -> NetworkResponseState.Loading
               is NetworkResponseState.Success -> NetworkResponseState.Success(it.result)
               is NetworkResponseState.Error -> NetworkResponseState.Error(it.exception)
            }
        }.flowOn(ioDispatcher)
    }

    override fun getSingleProductByIdFromApi(id: Int): Flow<NetworkResponseState<ResponseObject<Product>>> {
        return remoteDataSource.getSingleProductByIdFromApi(id).map {
            when(it) {
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Success -> NetworkResponseState.Success(it.result)
                is NetworkResponseState.Error -> NetworkResponseState.Error(it.exception)
            }
        }.flowOn(ioDispatcher)
    }

    override fun getAllCategoriesFromApi(): Flow<NetworkResponseState<ResponseObject<List<Category>>>> {
        return remoteDataSource.getAllCategoriesFromApi().map {
            when(it) {
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Success -> NetworkResponseState.Success(it.result)
                is NetworkResponseState.Error -> NetworkResponseState.Error(it.exception)
            }
        }.flowOn(ioDispatcher)
    }

    override fun getSingleCategoryFromApi(id: Int): Flow<NetworkResponseState<ResponseObject<Category>>> {
        return remoteDataSource.getSingleCategoryFromApi(id).map {
            when(it) {
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Success -> NetworkResponseState.Success(it.result)
                is NetworkResponseState.Error -> NetworkResponseState.Error(it.exception)
            }
        }.flowOn(ioDispatcher)
    }

    override fun getAllRoomTypesFromApi(): Flow<NetworkResponseState<ResponseObject<List<RoomType>>>> {
        return remoteDataSource.getAllRoomTypesFromApi().map {
            when(it) {
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Success -> NetworkResponseState.Success(it.result)
                is NetworkResponseState.Error -> NetworkResponseState.Error(it.exception)
            }
        }.flowOn(ioDispatcher)
    }

    override fun getAllSavedPlacementsFromApi(userId: String?): Flow<NetworkResponseState<ResponseObject<List<SavedPlacement>>>> {
        return remoteDataSource.getAllSavedPlacementsFromApi(userId).map {
            when(it) {
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Success -> NetworkResponseState.Success(it.result)
                is NetworkResponseState.Error -> NetworkResponseState.Error(it.exception)
            }
        }.flowOn(ioDispatcher)
    }

    override fun createNewSavedPlacementFromApi(data: SavedPlacement?): Flow<NetworkResponseState<ResponseObject<SavedPlacement>>> {
        return remoteDataSource.createNewSavedPlacementFromApi(data).map {
            when(it) {
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Success -> NetworkResponseState.Success(it.result)
                is NetworkResponseState.Error -> NetworkResponseState.Error(it.exception)
            }
        }.flowOn(ioDispatcher)
    }

    override fun updateSavedPlacementFromApi(data: SavedPlacement?): Flow<NetworkResponseState<ResponseObject<SavedPlacement>>> {
        return remoteDataSource.updatedSavedPlacementFromApi(data).map {
            when(it) {
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Success -> NetworkResponseState.Success(it.result)
                is NetworkResponseState.Error -> NetworkResponseState.Error(it.exception)
            }
        }.flowOn(ioDispatcher)
    }

    override fun deleteSavedPlacementFromApi(savedPlacementId: Int): Flow<NetworkResponseState<ResponseObject<SavedPlacement>>> {
        return remoteDataSource.deleteSavedPlacementFromApi(savedPlacementId).map {
            when(it) {
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Success -> NetworkResponseState.Success(it.result)
                is NetworkResponseState.Error -> NetworkResponseState.Error(it.exception)
            }
        }.flowOn(ioDispatcher)
    }

    override fun getAllIdeasFromApi(): Flow<NetworkResponseState<ResponseObject<List<Idea>>>> {
        return remoteDataSource.getAllIdeasFromApi().map {
            when(it) {
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Success -> NetworkResponseState.Success(it.result)
                is NetworkResponseState.Error -> NetworkResponseState.Error(it.exception)
            }
        }.flowOn(ioDispatcher)
    }

    override fun searchIdeasFromApi(): Flow<NetworkResponseState<ResponseObject<List<Idea>>>> {
        return remoteDataSource.searchIdeasFromApi().map {
            when(it) {
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Success -> NetworkResponseState.Success(it.result)
                is NetworkResponseState.Error -> NetworkResponseState.Error(it.exception)
            }
        }.flowOn(ioDispatcher)
    }

    override fun getSingleIdeaFromApi(ideaId: Int): Flow<NetworkResponseState<ResponseObject<Idea>>> {
        return remoteDataSource.getSingleIdeaFromApi(ideaId).map {
            when(it) {
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Success -> NetworkResponseState.Success(it.result)
                is NetworkResponseState.Error -> NetworkResponseState.Error(it.exception)
            }
        }.flowOn(ioDispatcher)
    }

    override fun createNewIdeaFromApi(data: Idea): Flow<NetworkResponseState<ResponseObject<Idea>>> {
        return remoteDataSource.createNewIdeaFromApi(data).map {
            when(it) {
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Success -> NetworkResponseState.Success(it.result)
                is NetworkResponseState.Error -> NetworkResponseState.Error(it.exception)
            }
        }.flowOn(ioDispatcher)
    }

    override fun deleteIdeaFromApi(ideaId: Int): Flow<NetworkResponseState<ResponseObject<Idea>>> {
        return remoteDataSource.deleteIdeaFromApi(ideaId).map {
            when(it) {
                is NetworkResponseState.Loading -> NetworkResponseState.Loading
                is NetworkResponseState.Success -> NetworkResponseState.Success(it.result)
                is NetworkResponseState.Error -> NetworkResponseState.Error(it.exception)
            }
        }.flowOn(ioDispatcher)
    }
}