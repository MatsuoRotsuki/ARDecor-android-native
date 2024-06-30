package com.soictnative.ardecor.domain.usecases.favourite

import com.soictnative.ardecor.data.entity.FavouriteProductEntity
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface GetFavouriteProductsUseCase {
    suspend operator fun invoke(userUid: String): Flow<NetworkResponseState<List<FavouriteProductEntity>>>
}