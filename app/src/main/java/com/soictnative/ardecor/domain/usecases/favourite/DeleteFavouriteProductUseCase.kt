package com.soictnative.ardecor.domain.usecases.favourite

import com.soictnative.ardecor.data.entity.FavouriteProductEntity

interface DeleteFavouriteProductUseCase {
    suspend operator fun invoke(product: FavouriteProductEntity)
}