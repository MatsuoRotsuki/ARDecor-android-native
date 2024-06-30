package com.soictnative.ardecor.domain.usecases.product

import androidx.paging.PagingData
import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.data.dto.ResponseObject
import com.soictnative.ardecor.domain.repository.RemoteRepository
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllProductsUseCaseImpl @Inject constructor(
    private val repository: RemoteRepository
) : GetAllProductsUseCase {
    override fun invoke(): Flow<NetworkResponseState<ResponseObject<List<Product>>>> {
        return repository.getAllProductsFromApi()
    }
}