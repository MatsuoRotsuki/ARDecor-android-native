package com.soictnative.ardecor.presentation.screens.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soictnative.ardecor.data.dto.Category
import com.soictnative.ardecor.domain.usecases.category.GetSingleCategoryUseCase
import com.soictnative.ardecor.util.NetworkResponseState
import com.soictnative.ardecor.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getSingleCategoryUseCase: GetSingleCategoryUseCase
): ViewModel() {

    private val _category = MutableLiveData<ScreenState<Category>>()
    val category: LiveData<ScreenState<Category>> get() = _category

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            savedStateHandle.get<Int>("categoryId")?.let { categoryId ->
                getSingleCategoryUseCase(categoryId).collect {
                    when(it) {
                        is NetworkResponseState.Error -> _category.postValue(ScreenState.Error(it.exception.message!!))
                        is NetworkResponseState.Loading -> _category.postValue(ScreenState.Loading)
                        is NetworkResponseState.Success -> _category.postValue(ScreenState.Success(it.result.data))
                    }
                }
            }
        }
    }
}