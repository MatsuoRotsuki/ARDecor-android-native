package com.soictnative.ardecor.data.source.remote

import com.soictnative.ardecor.data.dto.Category
import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.data.dto.ResponseObject
import com.soictnative.ardecor.data.dto.RoomType
import com.soictnative.ardecor.data.api.ApiService
import com.soictnative.ardecor.data.dto.Idea
import com.soictnative.ardecor.data.dto.SavedPlacement
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : RemoteDataSource {
    override fun getAllProductsFromApi(): Flow<NetworkResponseState<ResponseObject<List<Product>>>> {
        return flow {
            emit(NetworkResponseState.Loading)
            try {
                val response = apiService.getAllProducts()
                emit(NetworkResponseState.Success(response))
            } catch (e: Exception) {
                emit(NetworkResponseState.Error(e))
            }
        }
    }

    override fun searchProductsFromApi(
        keyword: String?,
        categoryId: Int?,
        sortBy: String?,
        minPrice: Float?,
        maxPrice: Float?
    ): Flow<NetworkResponseState<ResponseObject<List<Product>>>> {
        return flow {
            emit(NetworkResponseState.Loading)
            try {
                val response = apiService.searchProducts(
                    keyword = keyword,
                    categoryId = categoryId,
                    sortBy = sortBy,
                    minPrice = minPrice,
                    maxPrice = maxPrice
                )
                emit(NetworkResponseState.Success(response))
            } catch (e: Exception) {
                emit(NetworkResponseState.Error(e))
            }
        }
    }

    override fun getSingleProductByIdFromApi(id: Int): Flow<NetworkResponseState<ResponseObject<Product>>> {
        return flow {
            emit(NetworkResponseState.Loading)
            try {
                val response = apiService.getSingleProductById(id)
                emit(NetworkResponseState.Success(response))
            } catch (e: Exception) {
                emit(NetworkResponseState.Error(e))
            }
        }
    }

    override fun getAllCategoriesFromApi(): Flow<NetworkResponseState<ResponseObject<List<Category>>>> {
        return flow {
            emit(NetworkResponseState.Loading)
            try {
                val response = apiService.getAllCategories()
                emit(NetworkResponseState.Success(response))
            } catch (e: Exception) {
                emit(NetworkResponseState.Error(e))
            }
        }
    }

    override fun getSingleCategoryFromApi(id: Int): Flow<NetworkResponseState<ResponseObject<Category>>> {
        return flow {
            emit(NetworkResponseState.Loading)
            try {
                val response = apiService.getSingleCategoryById(id)
                emit(NetworkResponseState.Success(response))
            } catch (e: Exception) {
                emit(NetworkResponseState.Error(e))
            }
        }
    }


    override fun getAllRoomTypesFromApi(): Flow<NetworkResponseState<ResponseObject<List<RoomType>>>> {
        return flow {
            emit(NetworkResponseState.Loading)
            try {
                val response = apiService.getAllRoomTypes()
                emit(NetworkResponseState.Success(response))
            } catch (e: Exception) {
                emit(NetworkResponseState.Error(e))
            }
        }
    }

    override fun getAllSavedPlacementsFromApi(userId: String?): Flow<NetworkResponseState<ResponseObject<List<SavedPlacement>>>> {
        return flow {
            emit(NetworkResponseState.Loading)
            try {
                val response = apiService.getAllSavedPlacements(userId)
                emit(NetworkResponseState.Success(response))
            } catch (e: Exception) {
                emit(NetworkResponseState.Error(e))
            }
        }
    }

    override fun createNewSavedPlacementFromApi(data: SavedPlacement?): Flow<NetworkResponseState<ResponseObject<SavedPlacement>>> {
        return flow {
            emit(NetworkResponseState.Loading)
            try {
                val response = apiService.createNewSavedPlacement(data)
                emit(NetworkResponseState.Success(response))
            } catch (e: Exception) {
                emit(NetworkResponseState.Error(e))
            }
        }
    }

    override fun updatedSavedPlacementFromApi(data: SavedPlacement?): Flow<NetworkResponseState<ResponseObject<SavedPlacement>>> {
        return flow {
            emit(NetworkResponseState.Loading)
            try {
                val response = apiService.updateSavedPlacement(data)
                emit(NetworkResponseState.Success(response))
            } catch (e: Exception) {
                emit(NetworkResponseState.Error(e))
            }
        }
    }

    override fun deleteSavedPlacementFromApi(savedPlacementId: Int): Flow<NetworkResponseState<ResponseObject<SavedPlacement>>> {
        return flow {
            emit(NetworkResponseState.Loading)
            try {
                val response = apiService.deleteSavedPlacement(savedPlacementId)
                emit(NetworkResponseState.Success(response))
            } catch (e: Exception) {
                emit(NetworkResponseState.Error(e))
            }
        }
    }

    override fun getAllIdeasFromApi(): Flow<NetworkResponseState<ResponseObject<List<Idea>>>> {
        return flow {
            emit (NetworkResponseState.Loading)
            try {
                val response = apiService.getAllIdeas()
                emit(NetworkResponseState.Success(response))
            } catch (e: Exception) {
                emit(NetworkResponseState.Error(e))
            }
        }
    }

    override fun searchIdeasFromApi(): Flow<NetworkResponseState<ResponseObject<List<Idea>>>> {
        return flow {
            emit (NetworkResponseState.Loading)
            try {
                val response = apiService.searchIdeas()
                emit(NetworkResponseState.Success(response))
            } catch (e: Exception) {
                emit(NetworkResponseState.Error(e))
            }
        }
    }

    override fun getSingleIdeaFromApi(ideaId: Int): Flow<NetworkResponseState<ResponseObject<Idea>>> {
        return flow {
            emit (NetworkResponseState.Loading)
            try {
                val response = apiService.getSingleIdeaById(ideaId)
                emit(NetworkResponseState.Success(response))
            } catch (e: Exception) {
                emit(NetworkResponseState.Error(e))
            }
        }
    }

    override fun createNewIdeaFromApi(data: Idea): Flow<NetworkResponseState<ResponseObject<Idea>>> {
        return flow {
            emit(NetworkResponseState.Loading)
            try {
                val response = apiService.createNewIdea(data)
                emit(NetworkResponseState.Success(response))
            } catch (e: Exception) {
                emit(NetworkResponseState.Error(e))
            }
        }
    }

    override fun deleteIdeaFromApi(ideaId: Int): Flow<NetworkResponseState<ResponseObject<Idea>>> {
        return flow {
            emit (NetworkResponseState.Loading)
            try {
                val response = apiService.deleteIdea(ideaId)
                emit(NetworkResponseState.Success(response))
            } catch (e: Exception) {
                emit(NetworkResponseState.Error(e))
            }
        }
    }
}