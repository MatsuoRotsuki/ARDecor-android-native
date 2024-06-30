package com.soictnative.ardecor.domain.usecases.placements

import com.soictnative.ardecor.data.dto.ResponseObject
import com.soictnative.ardecor.data.dto.SavedPlacement
import com.soictnative.ardecor.domain.repository.RemoteRepository
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllSavedPlacementsUseCaseImpl @Inject constructor(
    private val repository: RemoteRepository
): GetAllSavedPlacementsUseCase {
    override fun invoke(userId: String?): Flow<NetworkResponseState<ResponseObject<List<SavedPlacement>>>> {
        return repository.getAllSavedPlacementsFromApi(userId)
    }
}