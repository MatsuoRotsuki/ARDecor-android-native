package com.soictnative.ardecor.domain.usecases.placements

import com.soictnative.ardecor.data.dto.ResponseObject
import com.soictnative.ardecor.data.dto.SavedPlacement
import com.soictnative.ardecor.domain.repository.RemoteRepository
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteSavedPlacementUseCaseImpl @Inject constructor(
    private val repository: RemoteRepository
) : DeleteSavedPlacementUseCase {
    override fun invoke(savedPlacementId: Int): Flow<NetworkResponseState<ResponseObject<SavedPlacement>>> {
        return repository.deleteSavedPlacementFromApi(savedPlacementId)
    }
}