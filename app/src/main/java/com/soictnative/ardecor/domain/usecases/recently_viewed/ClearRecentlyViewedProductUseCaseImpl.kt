package com.soictnative.ardecor.domain.usecases.recently_viewed

import com.soictnative.ardecor.domain.repository.LocalRepository
import javax.inject.Inject

class ClearRecentlyViewedProductUseCaseImpl @Inject constructor(
    private val repository: LocalRepository
) : ClearRecentlyViewedProductUseCase {
    override suspend fun invoke(userUid: String) {
        repository.clearRecentlyViewedProductsByUserFromDb(userUid)
    }

}