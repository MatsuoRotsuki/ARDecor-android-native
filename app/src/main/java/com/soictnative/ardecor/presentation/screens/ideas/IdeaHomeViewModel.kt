package com.soictnative.ardecor.presentation.screens.ideas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soictnative.ardecor.data.dto.Idea
import com.soictnative.ardecor.data.dto.RoomType
import com.soictnative.ardecor.domain.usecases.ideas.GetAllIdeasUseCase
import com.soictnative.ardecor.domain.usecases.room_types.GetAllRoomTypesUseCase
import com.soictnative.ardecor.util.NetworkResponseState
import com.soictnative.ardecor.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class IdeaHomeViewModel @Inject constructor(
    private val getAllRoomTypesUseCase: GetAllRoomTypesUseCase,
    private val getAllIdeasUseCase: GetAllIdeasUseCase
) : ViewModel() {

    private val _roomTypes = MutableLiveData<ScreenState<List<RoomType>>>()
    val roomType: LiveData<ScreenState<List<RoomType>>> get() = _roomTypes

    private val _idea = MutableLiveData<ScreenState<List<Idea>>>()
    val idea: LiveData<ScreenState<List<Idea>>> get() = _idea

    init {
        getAllRoomTypes()
        getAllIdeas()
    }

    fun getAllIdeas() {
        getAllIdeasUseCase().onEach {
            when (it) {
                is NetworkResponseState.Loading -> _idea.postValue(ScreenState.Loading)
                is NetworkResponseState.Error -> _idea.postValue(ScreenState.Error(it.exception.message!!))
                is NetworkResponseState.Success -> _idea.postValue(ScreenState.Success(it.result.data))
            }
        }.launchIn(viewModelScope)
    }

    private fun getAllRoomTypes() {
        getAllRoomTypesUseCase().onEach {
            when (it) {
                is NetworkResponseState.Loading -> _roomTypes.postValue(ScreenState.Loading)
                is NetworkResponseState.Success -> _roomTypes.postValue(ScreenState.Success(it.result.data))
                is NetworkResponseState.Error -> _roomTypes.postValue(ScreenState.Error(it.exception.message!!))
            }
        }.launchIn(viewModelScope)
    }
}