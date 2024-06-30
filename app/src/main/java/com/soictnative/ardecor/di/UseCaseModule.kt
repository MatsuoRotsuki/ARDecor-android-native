package com.soictnative.ardecor.di

import com.soictnative.ardecor.domain.usecases.category.CategoryUseCase
import com.soictnative.ardecor.domain.usecases.category.CategoryUseCaseImpl
import com.soictnative.ardecor.domain.usecases.category.GetSingleCategoryUseCase
import com.soictnative.ardecor.domain.usecases.category.GetSingleCategoryUseCaseImpl
import com.soictnative.ardecor.domain.usecases.favourite.DeleteFavouriteProductUseCase
import com.soictnative.ardecor.domain.usecases.favourite.DeleteFavouriteProductUseCaseImpl
import com.soictnative.ardecor.domain.usecases.favourite.GetFavouriteProductsUseCase
import com.soictnative.ardecor.domain.usecases.favourite.GetFavouriteProductsUseCaseImpl
import com.soictnative.ardecor.domain.usecases.favourite.InsertFavouriteProductUseCase
import com.soictnative.ardecor.domain.usecases.favourite.InsertFavouriteProductUseCaseImpl
import com.soictnative.ardecor.domain.usecases.ideas.CreateNewIdeaUseCase
import com.soictnative.ardecor.domain.usecases.ideas.CreateNewIdeaUseCaseImpl
import com.soictnative.ardecor.domain.usecases.ideas.DeleteIdeaUseCase
import com.soictnative.ardecor.domain.usecases.ideas.DeleteIdeaUseCaseImpl
import com.soictnative.ardecor.domain.usecases.ideas.GetAllIdeasUseCase
import com.soictnative.ardecor.domain.usecases.ideas.GetAllIdeasUseCaseImpl
import com.soictnative.ardecor.domain.usecases.ideas.GetSingleIdeaUseCase
import com.soictnative.ardecor.domain.usecases.ideas.GetSingleIdeaUseCaseImpl
import com.soictnative.ardecor.domain.usecases.ideas.SearchIdeasUseCase
import com.soictnative.ardecor.domain.usecases.ideas.SearchIdeasUseCaseImpl
import com.soictnative.ardecor.domain.usecases.placements.CreateNewSavedPlacementUseCase
import com.soictnative.ardecor.domain.usecases.placements.CreateNewSavedPlacementUseCaseImpl
import com.soictnative.ardecor.domain.usecases.placements.DeleteSavedPlacementUseCase
import com.soictnative.ardecor.domain.usecases.placements.DeleteSavedPlacementUseCaseImpl
import com.soictnative.ardecor.domain.usecases.placements.GetAllSavedPlacementsUseCase
import com.soictnative.ardecor.domain.usecases.placements.GetAllSavedPlacementsUseCaseImpl
import com.soictnative.ardecor.domain.usecases.placements.UpdateSavedPlacementUseCase
import com.soictnative.ardecor.domain.usecases.placements.UpdateSavedPlacementUseCaseImpl
import com.soictnative.ardecor.domain.usecases.product.GetAllPagingProductsUseCase
import com.soictnative.ardecor.domain.usecases.product.GetAllPagingProductsUseCaseImpl
import com.soictnative.ardecor.domain.usecases.product.GetAllProductsUseCase
import com.soictnative.ardecor.domain.usecases.product.GetAllProductsUseCaseImpl
import com.soictnative.ardecor.domain.usecases.product.GetSingleProductUseCase
import com.soictnative.ardecor.domain.usecases.product.GetSingleProductUseCaseImpl
import com.soictnative.ardecor.domain.usecases.product.SearchProductsUseCase
import com.soictnative.ardecor.domain.usecases.product.SearchProductsUseCaseImpl
import com.soictnative.ardecor.domain.usecases.recently_viewed.ClearRecentlyViewedProductUseCase
import com.soictnative.ardecor.domain.usecases.recently_viewed.ClearRecentlyViewedProductUseCaseImpl
import com.soictnative.ardecor.domain.usecases.recently_viewed.GetRecentlyViewedProductUseCase
import com.soictnative.ardecor.domain.usecases.recently_viewed.GetRecentlyViewedProductUseCaseImpl
import com.soictnative.ardecor.domain.usecases.recently_viewed.InsertRecentlyViewedProductUseCase
import com.soictnative.ardecor.domain.usecases.recently_viewed.InsertRecentlyViewedProductUseCaseImpl
import com.soictnative.ardecor.domain.usecases.room_types.GetAllRoomTypesUseCase
import com.soictnative.ardecor.domain.usecases.room_types.GetAllRoomTypesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    @ViewModelScoped
    abstract fun getAllProductsUseCase(
        getAllProductsUseCaseImpl: GetAllProductsUseCaseImpl
    ): GetAllProductsUseCase

    @Binds
    @ViewModelScoped
    abstract fun searchProductsUseCase(
        searchProductsUseCaseImpl: SearchProductsUseCaseImpl
    ): SearchProductsUseCase

    @Binds
    @ViewModelScoped
    abstract fun getSingleProductUseCase(
        getSingleProductUseCaseImpl: GetSingleProductUseCaseImpl
    ): GetSingleProductUseCase

    @Binds
    @ViewModelScoped
    abstract fun getAllCategoriesUseCase(
        getAllCategoriesUseCaseImpl: CategoryUseCaseImpl
    ): CategoryUseCase

    @Binds
    @ViewModelScoped
    abstract fun getAllSavedPlacementsUseCase(
        getAllSavedPlacementsUseCaseImpl: GetAllSavedPlacementsUseCaseImpl
    ): GetAllSavedPlacementsUseCase

    @Binds
    @ViewModelScoped
    abstract fun createNewSavedPlacementUseCase(
        createNewSavedPlacementUseCaseImpl: CreateNewSavedPlacementUseCaseImpl
    ): CreateNewSavedPlacementUseCase

    @Binds
    @ViewModelScoped
    abstract fun updateSavedPlacementUseCase(
        updateSavedPlacementUseCaseImpl: UpdateSavedPlacementUseCaseImpl
    ): UpdateSavedPlacementUseCase

    @Binds
    @ViewModelScoped
    abstract fun deleteSavedPlacementUseCase(
        deleteSavedPlacementUseCaseImpl: DeleteSavedPlacementUseCaseImpl
    ): DeleteSavedPlacementUseCase

    @Binds
    @ViewModelScoped
    abstract fun getAllRoomTypesUseCase(
        getAllRoomTypesUseCaseImpl: GetAllRoomTypesUseCaseImpl
    ): GetAllRoomTypesUseCase

    @Binds
    @ViewModelScoped
    abstract fun getSingleCategoryUseCase(
        getSingleCategoryUseCaseImpl: GetSingleCategoryUseCaseImpl
    ): GetSingleCategoryUseCase

    @Binds
    @ViewModelScoped
    abstract fun getAllIdeasUseCase(
        getAllIdeasUseCaseImpl: GetAllIdeasUseCaseImpl
    ): GetAllIdeasUseCase

    @Binds
    @ViewModelScoped
    abstract fun searchIdeasUseCase(
        searchIdeasUseCaseImpl: SearchIdeasUseCaseImpl
    ): SearchIdeasUseCase

    @Binds
    @ViewModelScoped
    abstract fun getSingleIdeaUseCase(
        getSingleIdeaUseCaseImpl: GetSingleIdeaUseCaseImpl
    ): GetSingleIdeaUseCase

    @Binds
    @ViewModelScoped
    abstract fun createNewIdeaUseCase(
        createNewIdeaUseCaseImpl: CreateNewIdeaUseCaseImpl
    ): CreateNewIdeaUseCase

    @Binds
    @ViewModelScoped
    abstract fun deleteIdeaUseCase(
        deleteIdeaUseCaseImpl: DeleteIdeaUseCaseImpl
    ): DeleteIdeaUseCase

    @Binds
    @ViewModelScoped
    abstract fun getAllPagingProductUseCase(
        getAllPagingProductsUseCaseImpl: GetAllPagingProductsUseCaseImpl
    ): GetAllPagingProductsUseCase

    @Binds
    @ViewModelScoped
    abstract fun getFavouriteProductUsecase(
        getFavouriteProductsUseCaseImpl: GetFavouriteProductsUseCaseImpl
    ): GetFavouriteProductsUseCase

    @Binds
    @ViewModelScoped
    abstract fun insertFavouriteProductUseCase(
        insertFavouriteProductUseCaseImpl: InsertFavouriteProductUseCaseImpl
    ): InsertFavouriteProductUseCase

    @Binds
    @ViewModelScoped
    abstract fun deleteFavouriteProductUseCase(
        deleteFavouriteProductUseCaseImpl: DeleteFavouriteProductUseCaseImpl
    ): DeleteFavouriteProductUseCase

    @Binds
    @ViewModelScoped
    abstract fun getRecentlyViewedProductUseCase(
        getRecentlyViewedProductUseCaseImpl: GetRecentlyViewedProductUseCaseImpl
    ): GetRecentlyViewedProductUseCase

    @Binds
    @ViewModelScoped
    abstract fun insertRecentlyViewedProductUseCase(
        insertRecentlyViewedProductUseCaseImpl: InsertRecentlyViewedProductUseCaseImpl
    ): InsertRecentlyViewedProductUseCase

    @Binds
    @ViewModelScoped
    abstract fun clearRecentlyViewedProductUseCase(
        clearRecentlyViewedProductUseCaseImpl: ClearRecentlyViewedProductUseCaseImpl
    ): ClearRecentlyViewedProductUseCase
}