package com.soictnative.ardecor.domain.usecases.recently_viewed

interface ClearRecentlyViewedProductUseCase {
    suspend operator fun invoke(userUid: String)
}