package com.soictnative.ardecor.domain.usecases.placements

import com.soictnative.ardecor.data.dto.ResponseObject
import com.soictnative.ardecor.data.dto.SavedPlacement
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface CreateNewSavedPlacementUseCase {
    operator fun invoke(data: SavedPlacement?): Flow<NetworkResponseState<ResponseObject<SavedPlacement>>>
}