package com.soictnative.ardecor.domain.usecases.product

import androidx.paging.PagingData
import com.soictnative.ardecor.data.dto.Product
import kotlinx.coroutines.flow.Flow

interface GetAllPagingProductsUseCase {
    operator fun invoke(): Flow<PagingData<Product>>
}