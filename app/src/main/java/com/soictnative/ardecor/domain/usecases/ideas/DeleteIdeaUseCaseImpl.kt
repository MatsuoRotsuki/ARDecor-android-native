package com.soictnative.ardecor.domain.usecases.ideas

import com.soictnative.ardecor.data.dto.Idea
import com.soictnative.ardecor.data.dto.ResponseObject
import com.soictnative.ardecor.domain.repository.RemoteRepository
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteIdeaUseCaseImpl @Inject constructor(
    private val repository: RemoteRepository
): DeleteIdeaUseCase {
    override fun invoke(ideaId: Int): Flow<NetworkResponseState<ResponseObject<Idea>>> {
        return repository.deleteIdeaFromApi(ideaId)
    }
}