package com.soictnative.ardecor.domain.usecases.favourite

import com.soictnative.ardecor.data.entity.FavouriteProductEntity

interface InsertFavouriteProductUseCase {
    suspend operator fun invoke(product: FavouriteProductEntity)
}