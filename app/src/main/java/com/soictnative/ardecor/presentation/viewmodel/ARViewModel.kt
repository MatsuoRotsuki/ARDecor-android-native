package com.soictnative.ardecor.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.soictnative.ardecor.data.dto.Category
import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.data.dto.Settings
import com.soictnative.ardecor.data.dto.SavedPlacement
import com.soictnative.ardecor.domain.usecases.category.CategoryUseCase
import com.soictnative.ardecor.domain.usecases.placements.CreateNewSavedPlacementUseCase
import com.soictnative.ardecor.domain.usecases.product.GetAllProductsUseCase
import com.soictnative.ardecor.domain.usecases.product.SearchProductsUseCase
import com.soictnative.ardecor.util.NetworkResponseState
import com.soictnative.ardecor.util.ScreenState
import com.unity3d.player.UnityPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ARViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val categoryUseCase: CategoryUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val createNewSavedPlacementUseCase: CreateNewSavedPlacementUseCase
): ViewModel() {

    private val _inventoryProducts = MutableStateFlow<ScreenState<List<Product>>>(ScreenState.Unspecified)
    val inventoryProducts: StateFlow<ScreenState<List<Product>>> = _inventoryProducts.asStateFlow()

    private val _categories = MutableStateFlow<ScreenState<List<Category>>>(ScreenState.Unspecified)
    val categories: StateFlow<ScreenState<List<Category>>> get() = _categories

    private val _contextProducts = MutableStateFlow<List<Product>>(emptyList())
    val contextProducts: StateFlow<List<Product>> = _contextProducts.asStateFlow()

    private val _settings = MutableStateFlow(
        Settings(true, true, true, true)
    )
    val settings = _settings.asStateFlow()

    private val _createNewPlacement = MutableLiveData<ScreenState<SavedPlacement>>()
    val createdNewPlacement: LiveData<ScreenState<SavedPlacement>> get() = _createNewPlacement

    private val _userId = mutableStateOf("")
    val userId: State<String> get() = _userId

    init {
        fetchInventoryProducts()
        fetchCategories()
    }

    private fun fetchInventoryProducts() {
        getAllProductsUseCase().onEach {
            when(it) {
                is NetworkResponseState.Loading -> _inventoryProducts.emit(ScreenState.Loading)
                is NetworkResponseState.Success -> _inventoryProducts.emit(ScreenState.Success(it.result.data))
                is NetworkResponseState.Error -> _inventoryProducts.emit(ScreenState.Error(it.exception.message!!))
            }
        }.launchIn(viewModelScope)
    }

    private fun fetchCategories() {
        categoryUseCase().onEach {
            when(it) {
                is NetworkResponseState.Error -> _categories.emit(ScreenState.Error(it.exception.message!!))
                is NetworkResponseState.Loading -> _categories.emit(ScreenState.Loading)
                is NetworkResponseState.Success -> _categories.emit(ScreenState.Success(it.result.data))
            }
        }.launchIn(viewModelScope)
    }

    fun fetchProductOnCategory(categoryId: Int) {
        searchProductsUseCase(
            categoryId = categoryId
        ).onEach {
            when (it) {
                is NetworkResponseState.Loading -> _inventoryProducts.emit(ScreenState.Loading)
                is NetworkResponseState.Success -> _inventoryProducts.emit(ScreenState.Success(it.result.data))
                is NetworkResponseState.Error -> _inventoryProducts.emit(ScreenState.Error(it.exception.message!!))
            }
        }.launchIn(viewModelScope)
    }

    fun setSettingsStateFromJson(json: String?)
    {
        val gson = Gson()
        val newSettingsState: Settings = gson.fromJson(json, Settings::class.java)
        viewModelScope.launch {
            _settings.emit(newSettingsState)
        }
    }

    private fun setSettingsState(newState: Settings)
    {
        val gson = Gson()
        viewModelScope.launch {
            _settings.emit(newState)
        }
        val newStateJson = gson.toJson(newState)

        try {
            UnityPlayer.UnitySendMessage("SettingsManager", "AndroidSetState", newStateJson)
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    fun gizmoSwitchChanged(isChecked: Boolean)
    {
        val currentState = _settings.value
        val newState = currentState.copy(isGizmoVisible = isChecked)
        setSettingsState(newState)
    }

    fun dimensionsSwitchChanged(isChecked: Boolean)
    {
        val currentState = _settings.value
        val newState = currentState.copy(isDimensionsVisible = isChecked)
        setSettingsState(newState)
    }

    fun planeSwitchChanged(isChecked: Boolean)
    {
        val currentState = _settings.value
        val newState = currentState.copy(isPlaneVisible = isChecked)
        setSettingsState(newState)
    }

    fun scalingSwitchChanged(isChecked: Boolean)
    {
        val currentState = _settings.value
        val newState = currentState.copy(isScalingEnabled = isChecked)
        setSettingsState(newState)
    }

    fun setFurnitureContext(json: String?) {
        val gson = Gson()
        val furnitureContextProductList: List<Product> = gson.fromJson(json, object : TypeToken<List<Product>>() {}.type)
        viewModelScope.launch {
            _contextProducts.emit(furnitureContextProductList)
        }
    }

    fun setUserId(userId: String) {
        viewModelScope.launch {
            _userId.value = userId
        }
    }

    fun createNewSavePlacement(placementJson: String, placementName: String) {
        val isUserIdEmpty = _userId.value.isEmpty()
        if (!isUserIdEmpty) {
            val newSavedPlacement = SavedPlacement(
                id = 0,
                name = placementName,
                placements = placementJson,
                user_id = _userId.value,
                created_at = "",
                updated_at = "",
            )
            createNewSavedPlacementUseCase(newSavedPlacement).onEach {
                when (it) {
                    is NetworkResponseState.Loading -> _createNewPlacement.postValue(ScreenState.Loading)
                    is NetworkResponseState.Success -> _createNewPlacement.postValue(ScreenState.Success(it.result.data))
                    is NetworkResponseState.Error -> _createNewPlacement.postValue(ScreenState.Error(it.exception.message!!))
                }
            }.launchIn(viewModelScope)
        }
    }
}