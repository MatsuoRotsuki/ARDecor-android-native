package com.soictnative.ardecor.domain.usecases.placements

import com.soictnative.ardecor.data.dto.ResponseObject
import com.soictnative.ardecor.data.dto.SavedPlacement
import com.soictnative.ardecor.domain.repository.RemoteRepository
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateSavedPlacementUseCaseImpl @Inject constructor(
    private val repository: RemoteRepository
): UpdateSavedPlacementUseCase {
    override fun invoke(data: SavedPlacement?): Flow<NetworkResponseState<ResponseObject<SavedPlacement>>> {
        return repository.updateSavedPlacementFromApi(data)
    }
}