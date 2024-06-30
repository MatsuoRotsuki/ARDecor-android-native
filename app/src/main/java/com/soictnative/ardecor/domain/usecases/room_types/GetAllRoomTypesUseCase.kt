package com.soictnative.ardecor.domain.usecases.room_types

import com.soictnative.ardecor.data.dto.ResponseObject
import com.soictnative.ardecor.data.dto.RoomType
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface GetAllRoomTypesUseCase {
    operator fun invoke(): Flow<NetworkResponseState<ResponseObject<List<RoomType>>>>
}