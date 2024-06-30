package com.soictnative.ardecor.domain.usecases.favourite

import com.soictnative.ardecor.data.entity.FavouriteProductEntity
import com.soictnative.ardecor.domain.repository.LocalRepository
import javax.inject.Inject

class InsertFavouriteProductUseCaseImpl @Inject constructor(
    private val repository: LocalRepository
) : InsertFavouriteProductUseCase {
    override suspend fun invoke(product: FavouriteProductEntity) {
        repository.insertFavouriteProductToDb(product)
    }

}