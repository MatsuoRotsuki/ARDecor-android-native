package com.soictnative.ardecor.domain.usecases.favourite

import com.soictnative.ardecor.data.entity.FavouriteProductEntity
import com.soictnative.ardecor.domain.repository.LocalRepository
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavouriteProductsUseCaseImpl @Inject constructor(
    private val repository: LocalRepository
) : GetFavouriteProductsUseCase {
    override suspend fun invoke(userUid: String): Flow<NetworkResponseState<List<FavouriteProductEntity>>> {
        return repository.getFavouriteProductsByUserFromDb(userUid)
    }
}