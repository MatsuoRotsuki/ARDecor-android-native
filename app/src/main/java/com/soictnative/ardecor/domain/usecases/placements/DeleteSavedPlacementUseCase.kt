package com.soictnative.ardecor.domain.usecases.placements

import com.soictnative.ardecor.data.dto.ResponseObject
import com.soictnative.ardecor.data.dto.SavedPlacement
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface DeleteSavedPlacementUseCase {
    operator fun invoke(savedPlacementId: Int): Flow<NetworkResponseState<ResponseObject<SavedPlacement>>>
}