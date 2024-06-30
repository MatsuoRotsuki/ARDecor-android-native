package com.soictnative.ardecor.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soictnative.ardecor.data.dto.SavedPlacement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavePlacementViewModel @Inject constructor(
): ViewModel() {

    private val _startDestination = mutableStateOf("")
    val startDestination: State<String> get() = _startDestination

    private val _placementJson = mutableStateOf("")

    private val _placementDataToCreateIdea = mutableStateOf<SavedPlacement?>( null )
    val placementDatatoCreateIdea: State<SavedPlacement?> get() = _placementDataToCreateIdea
    val placementJson: State<String> get() = _placementJson

    fun changeScreenTo(route: String) {
        viewModelScope.launch {
            _startDestination.value = route
        }
    }

    fun updatePlacementJson(placementJson: String) {
        viewModelScope.launch {
            _placementJson.value = placementJson
        }
    }

    fun setPlacementData(placementData: SavedPlacement) {
        viewModelScope.launch {
            _placementDataToCreateIdea.value = placementData
        }
    }
}