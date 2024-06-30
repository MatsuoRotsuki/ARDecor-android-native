package com.soictnative.ardecor.data.api

import com.soictnative.ardecor.data.dto.Category
import com.soictnative.ardecor.data.dto.Idea
import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.data.dto.ResponseObject
import com.soictnative.ardecor.data.dto.RoomType
import com.soictnative.ardecor.data.dto.SavedPlacement
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("products")
    suspend fun getAllProducts(
        @Query("page") page: Int = 1
    ): ResponseObject<List<Product>>

    @GET("products/search")
    suspend fun searchProducts(
        @Query("keyword") keyword: String? = null,
        @Query("category_id") categoryId: Int? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("minPrice") minPrice: Float? = null,
        @Query("maxPrice") maxPrice: Float? = null,
    ): ResponseObject<List<Product>>

    @GET("products/{id}")
    suspend fun getSingleProductById(@Path("id") id: Int) : ResponseObject<Product>

    @GET("categories")
    suspend fun getAllCategories(): ResponseObject<List<Category>>

    @GET("categories/{id}")
    suspend fun getSingleCategoryById(@Path("id") id: Int): ResponseObject<Category>

    @GET("room-types")
    suspend fun getAllRoomTypes(): ResponseObject<List<RoomType>>

    @GET("placements")
    suspend fun getAllSavedPlacements(@Query("user_id") userId: String? = null): ResponseObject<List<SavedPlacement>>

    @POST("placements")
    suspend fun createNewSavedPlacement(@Body data: SavedPlacement?): ResponseObject<SavedPlacement>

    @PUT("placements")
    suspend fun updateSavedPlacement(@Body data: SavedPlacement?): ResponseObject<SavedPlacement>

    @DELETE("placements/{id}")
    suspend fun deleteSavedPlacement(@Path("id") savedPlacementId: Int): ResponseObject<SavedPlacement>

    @GET("ideas")
    suspend fun getAllIdeas(): ResponseObject<List<Idea>>

    @GET("ideas/search")
    suspend fun searchIdeas(
        @Query("q") q: String? = null,
        @Query("room_type_id") roomTypeId: Int? = null,
    ): ResponseObject<List<Idea>>

    @GET("ideas/{id}")
    suspend fun getSingleIdeaById(@Path("id") id: Int): ResponseObject<Idea>

    @POST("ideas")
    suspend fun createNewIdea(@Body data: Idea): ResponseObject<Idea>

    @DELETE("ideas/{id}")
    suspend fun deleteIdea(ideaId: Int): ResponseObject<Idea>
}