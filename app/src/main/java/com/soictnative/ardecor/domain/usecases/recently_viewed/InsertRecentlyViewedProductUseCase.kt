package com.soictnative.ardecor.domain.usecases.recently_viewed

import com.soictnative.ardecor.data.entity.RecentlyViewedProductEntity

interface InsertRecentlyViewedProductUseCase {
    suspend operator fun invoke(product: RecentlyViewedProductEntity)
}