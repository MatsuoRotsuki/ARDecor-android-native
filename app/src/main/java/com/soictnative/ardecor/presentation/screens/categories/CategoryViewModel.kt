package com.soictnative.ardecor.presentation.screens.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soictnative.ardecor.data.dto.Category
import com.soictnative.ardecor.domain.usecases.category.CategoryUseCase
import com.soictnative.ardecor.util.NetworkResponseState
import com.soictnative.ardecor.util.ScreenState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import javax.inject.Inject

class CategoryViewModel @Inject constructor(
    private val getAllCategoryUsecase: CategoryUseCase,
) : ViewModel() {

    private val _categories = MutableLiveData<ScreenState<List<Category>>>()
    val categories: LiveData<ScreenState<List<Category>>> get() = _categories

    init {
        getAllCategories()
    }

    private fun getAllCategories() {
        getAllCategoryUsecase().onEach {
            when(it) {
                is NetworkResponseState.Error -> _categories.postValue(ScreenState.Error(it.exception.message!!))
                is NetworkResponseState.Loading -> _categories.postValue(ScreenState.Loading)
                is NetworkResponseState.Success -> _categories.postValue(ScreenState.Success(it.result.data))
            }
        }.launchIn(viewModelScope)
    }
}