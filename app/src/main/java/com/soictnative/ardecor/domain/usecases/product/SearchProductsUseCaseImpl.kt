package com.soictnative.ardecor.domain.usecases.product

import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.data.dto.ResponseObject
import com.soictnative.ardecor.domain.repository.RemoteRepository
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchProductsUseCaseImpl @Inject constructor(
    private val repository: RemoteRepository
) : SearchProductsUseCase {
    override fun invoke(
        keyword: String?,
        categoryId: Int?,
        sortBy: String?,
        minPrice: Float?,
        maxPrice: Float?
    ): Flow<NetworkResponseState<ResponseObject<List<Product>>>> {
        return repository.searchProductsFromApi(
            keyword = keyword,
            categoryId = categoryId,
            sortBy = sortBy,
            minPrice = minPrice,
            maxPrice = maxPrice
        )
    }
}