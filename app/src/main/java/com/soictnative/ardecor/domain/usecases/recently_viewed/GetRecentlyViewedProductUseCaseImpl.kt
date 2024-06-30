package com.soictnative.ardecor.domain.usecases.recently_viewed

import com.soictnative.ardecor.data.entity.RecentlyViewedProductEntity
import com.soictnative.ardecor.domain.repository.LocalRepository
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentlyViewedProductUseCaseImpl @Inject constructor(
    private val repository: LocalRepository
) : GetRecentlyViewedProductUseCase {
    override suspend fun invoke(userUid: String): Flow<NetworkResponseState<List<RecentlyViewedProductEntity>>> {
        return repository.getRecentlyViewedProductsByUserFromDb(userUid)
    }
}