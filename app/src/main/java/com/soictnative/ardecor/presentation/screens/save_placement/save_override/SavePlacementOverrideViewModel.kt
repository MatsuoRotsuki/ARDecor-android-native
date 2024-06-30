package com.soictnative.ardecor.presentation.screens.save_placement.save_override

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.soictnative.ardecor.data.dto.SavedPlacement
import com.soictnative.ardecor.domain.usecases.placements.GetAllSavedPlacementsUseCase
import com.soictnative.ardecor.domain.usecases.placements.UpdateSavedPlacementUseCase
import com.soictnative.ardecor.util.NetworkResponseState
import com.soictnative.ardecor.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavePlacementOverrideViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val getAllSavedPlacementsUseCase: GetAllSavedPlacementsUseCase,
    private val updateSavedPlacementUseCase: UpdateSavedPlacementUseCase,
): ViewModel() {

    private val _placements = MutableLiveData<ScreenState<List<SavedPlacement>>>()
    val placements: LiveData<ScreenState<List<SavedPlacement>>> get() = _placements

    private val _selectedPlacement = mutableStateOf<SavedPlacement?>(null)
    val selectedPlacement: State<SavedPlacement?> get() = _selectedPlacement

    private val _newPlacement = MutableLiveData<ScreenState<SavedPlacement>>()
    val newPlacement: LiveData<ScreenState<SavedPlacement>> get() = _newPlacement

    init {
        getAllSavedPlacementsByUser()
    }

    fun onOptionSelected(data: SavedPlacement?) {
        viewModelScope.launch {
            _selectedPlacement.value = data
        }
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

    fun savePlacementOverride(placementJson: String) {
        val newPlacement = _selectedPlacement.value?.copy(placements = placementJson)
        updateSavedPlacementUseCase(newPlacement).onEach {
            when(it) {
                is NetworkResponseState.Loading -> _newPlacement.postValue(ScreenState.Loading)
                is NetworkResponseState.Error -> _newPlacement.postValue(ScreenState.Error(it.exception.message!!))
                is NetworkResponseState.Success -> _newPlacement.postValue(ScreenState.Success(it.result.data))
            }
        }.launchIn(viewModelScope)
    }
}