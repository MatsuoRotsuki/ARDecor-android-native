package com.soictnative.ardecor.domain.usecases.product

import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.data.dto.ResponseObject
import com.soictnative.ardecor.domain.repository.RemoteRepository
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSingleProductUseCaseImpl @Inject constructor(
    private val repository: RemoteRepository
) : GetSingleProductUseCase {
    override fun invoke(id: Int): Flow<NetworkResponseState<ResponseObject<Product>>> {
        return repository.getSingleProductByIdFromApi(id)
    }
}