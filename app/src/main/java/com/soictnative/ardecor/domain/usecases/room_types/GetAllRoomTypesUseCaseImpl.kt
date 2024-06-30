package com.soictnative.ardecor.domain.usecases.room_types

import com.soictnative.ardecor.data.dto.ResponseObject
import com.soictnative.ardecor.data.dto.RoomType
import com.soictnative.ardecor.domain.repository.RemoteRepository
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllRoomTypesUseCaseImpl @Inject constructor(
    private val repository: RemoteRepository
) : GetAllRoomTypesUseCase {
    override fun invoke(): Flow<NetworkResponseState<ResponseObject<List<RoomType>>>> {
        return repository.getAllRoomTypesFromApi()
    }
}