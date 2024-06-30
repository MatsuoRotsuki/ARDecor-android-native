package com.soictnative.ardecor.domain.usecases.category

import com.soictnative.ardecor.data.dto.Category
import com.soictnative.ardecor.data.dto.ResponseObject
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface GetSingleCategoryUseCase {
    operator fun invoke(id: Int): Flow<NetworkResponseState<ResponseObject<Category>>>
}