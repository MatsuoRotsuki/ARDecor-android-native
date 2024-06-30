package com.soictnative.ardecor.domain.usecases.recently_viewed

import com.soictnative.ardecor.data.entity.RecentlyViewedProductEntity
import com.soictnative.ardecor.domain.repository.LocalRepository
import javax.inject.Inject

class InsertRecentlyViewedProductUseCaseImpl @Inject constructor(
    private val repository: LocalRepository
) : InsertRecentlyViewedProductUseCase {
    override suspend fun invoke(product: RecentlyViewedProductEntity) {
        repository.insertRecentlyViewedProductToDb(product)
    }
}