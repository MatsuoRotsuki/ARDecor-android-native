package com.soictnative.ardecor.presentation.screens.save_placement.save_current

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.soictnative.ardecor.data.dto.SavedPlacement
import com.soictnative.ardecor.domain.usecases.placements.DeleteSavedPlacementUseCase
import com.soictnative.ardecor.domain.usecases.placements.GetAllSavedPlacementsUseCase
import com.soictnative.ardecor.util.NetworkResponseState
import com.soictnative.ardecor.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SavedPlacementByUserViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val getAllSavedPlacementsUseCase: GetAllSavedPlacementsUseCase,
    private val deleteSavedPlacementUseCase: DeleteSavedPlacementUseCase
) : ViewModel() {

    private val _placements = MutableLiveData<ScreenState<List<SavedPlacement>>>()
    val placements: LiveData<ScreenState<List<SavedPlacement>>> get() = _placements

    private val _deleteTask = MutableLiveData<ScreenState<SavedPlacement>>()
    val deleteTask: LiveData<ScreenState<SavedPlacement>> get() = _deleteTask

    init {
        getAllSavedPlacementsByUser()
    }

    fun getAllSavedPlacementsByUser() {
        val user = firebaseAuth.currentUser ?: return
        getAllSavedPlacementsUseCase(userId = user.uid).onEach {
            when (it) {
                is NetworkResponseState.Loading -> _placements.postValue(ScreenState.Loading)
                is NetworkResponseState.Error -> _placements.postValue(ScreenState.Error(it.exception.message!!))
                is NetworkResponseState.Success -> _placements.postValue(ScreenState.Success(it.result.data))
            }
        }.launchIn(viewModelScope)
    }

    fun deleteSavedPlacement(data: SavedPlacement) {
        deleteSavedPlacementUseCase(data.id).onEach {
            when (it) {
                is NetworkResponseState.Loading -> _deleteTask.postValue(ScreenState.Loading)
                is NetworkResponseState.Error -> _deleteTask.postValue(ScreenState.Error(it.exception.message!!))
                is NetworkResponseState.Success -> _deleteTask.postValue(ScreenState.Success(it.result.data))
            }
        }.launchIn(viewModelScope)
    }
}