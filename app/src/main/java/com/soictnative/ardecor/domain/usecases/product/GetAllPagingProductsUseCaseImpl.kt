package com.soictnative.ardecor.domain.usecases.product

import androidx.paging.PagingData
import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.domain.repository.RemoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPagingProductsUseCaseImpl @Inject constructor(
    private val repository: RemoteRepository
) : GetAllPagingProductsUseCase {
    override fun invoke(): Flow<PagingData<Product>> {
        return repository.getAllProducts()
    }
}