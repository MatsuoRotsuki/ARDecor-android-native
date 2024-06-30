package com.soictnative.ardecor.domain.usecases.product

import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.data.dto.ResponseObject
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface SearchProductsUseCase {
    operator fun invoke(
        keyword: String? = null,
        categoryId: Int? = null,
        sortBy: String? = null,
        minPrice: Float? = null,
        maxPrice: Float? = null,
    ): Flow<NetworkResponseState<ResponseObject<List<Product>>>>
}