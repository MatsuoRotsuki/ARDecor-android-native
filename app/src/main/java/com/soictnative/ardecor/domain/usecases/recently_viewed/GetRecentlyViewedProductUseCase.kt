package com.soictnative.ardecor.domain.usecases.recently_viewed

import com.soictnative.ardecor.data.entity.RecentlyViewedProductEntity
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface GetRecentlyViewedProductUseCase {
    suspend operator fun invoke(userUid: String): Flow<NetworkResponseState<List<RecentlyViewedProductEntity>>>
}