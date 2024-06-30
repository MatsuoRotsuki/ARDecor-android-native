package com.soictnative.ardecor.domain.repository

import androidx.paging.PagingData
import com.soictnative.ardecor.data.dto.Category
import com.soictnative.ardecor.data.dto.Idea
import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.data.dto.ResponseObject
import com.soictnative.ardecor.data.dto.RoomType
import com.soictnative.ardecor.data.dto.SavedPlacement
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {

    /*
    * Test paging
    * */
    fun getAllProducts(): Flow<PagingData<Product>>

    fun getAllProductsFromApi(): Flow<NetworkResponseState<ResponseObject<List<Product>>>>

    fun searchProductsFromApi(
        keyword: String? = null,
        categoryId: Int? = null,
        sortBy: String? = null,
        minPrice: Float? = null,
        maxPrice: Float? = null,
    ): Flow<NetworkResponseState<ResponseObject<List<Product>>>>

    fun getSingleProductByIdFromApi(id: Int): Flow<NetworkResponseState<ResponseObject<Product>>>

    fun getAllCategoriesFromApi(): Flow<NetworkResponseState<ResponseObject<List<Category>>>>

    fun getSingleCategoryFromApi(id: Int): Flow<NetworkResponseState<ResponseObject<Category>>>

    fun getAllRoomTypesFromApi(): Flow<NetworkResponseState<ResponseObject<List<RoomType>>>>

    fun getAllSavedPlacementsFromApi(userId: String? = null): Flow<NetworkResponseState<ResponseObject<List<SavedPlacement>>>>

    fun createNewSavedPlacementFromApi(data: SavedPlacement?): Flow<NetworkResponseState<ResponseObject<SavedPlacement>>>

    fun updateSavedPlacementFromApi(data: SavedPlacement?): Flow<NetworkResponseState<ResponseObject<SavedPlacement>>>

    fun deleteSavedPlacementFromApi(savedPlacementId: Int): Flow<NetworkResponseState<ResponseObject<SavedPlacement>>>

    fun getAllIdeasFromApi(): Flow<NetworkResponseState<ResponseObject<List<Idea>>>>

    fun searchIdeasFromApi(): Flow<NetworkResponseState<ResponseObject<List<Idea>>>>

    fun getSingleIdeaFromApi(ideaId: Int): Flow<NetworkResponseState<ResponseObject<Idea>>>

    fun createNewIdeaFromApi(data: Idea): Flow<NetworkResponseState<ResponseObject<Idea>>>

    fun deleteIdeaFromApi(ideaId: Int): Flow<NetworkResponseState<ResponseObject<Idea>>>
}