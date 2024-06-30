package com.soictnative.ardecor.domain.usecases.category

import com.soictnative.ardecor.data.dto.Category
import com.soictnative.ardecor.data.dto.ResponseObject
import com.soictnative.ardecor.domain.repository.RemoteRepository
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSingleCategoryUseCaseImpl @Inject constructor(
    private val repository: RemoteRepository
) : GetSingleCategoryUseCase {
    override fun invoke(id: Int): Flow<NetworkResponseState<ResponseObject<Category>>> {
        return repository.getSingleCategoryFromApi(id)
    }
}