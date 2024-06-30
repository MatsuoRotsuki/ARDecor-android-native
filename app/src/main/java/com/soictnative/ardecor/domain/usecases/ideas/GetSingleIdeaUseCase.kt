package com.soictnative.ardecor.domain.usecases.ideas

import com.soictnative.ardecor.data.dto.Idea
import com.soictnative.ardecor.data.dto.ResponseObject
import com.soictnative.ardecor.util.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface GetSingleIdeaUseCase {
    operator fun invoke(ideaId: Int): Flow<NetworkResponseState<ResponseObject<Idea>>>
}