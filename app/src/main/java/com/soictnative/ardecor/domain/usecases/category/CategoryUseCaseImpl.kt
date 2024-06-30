package com.soictnative.ardecor.domain.usecases.category

import com.soictnative.ardecor.data.dto.Category
import com.soictnative.ardecor.data.dto.ResponseObject
import com.soictnative.ardecor.domain.repository.RemoteRepository
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryUseCaseImpl @Inject constructor(
    private val remoteRepository: RemoteRepository
): CategoryUseCase {
    override fun invoke(): Flow<NetworkResponseState<ResponseObject<List<Category>>>> {
        return remoteRepository.getAllCategoriesFromApi()
    }
}