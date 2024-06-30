package com.soictnative.ardecor.domain.usecases.favourite

import com.soictnative.ardecor.data.entity.FavouriteProductEntity
import com.soictnative.ardecor.domain.repository.LocalRepository
import javax.inject.Inject

class DeleteFavouriteProductUseCaseImpl @Inject constructor(
    private val repository: LocalRepository
) : DeleteFavouriteProductUseCase {
    override suspend fun invoke(product: FavouriteProductEntity) {
        repository.deleteFavouriteProductFromDb(product)
    }
}