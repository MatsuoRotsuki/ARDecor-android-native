package com.soictnative.ardecor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soictnative.ardecor.data.dto.Category
import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.domain.usecases.category.CategoryUseCase
import com.soictnative.ardecor.domain.usecases.product.GetAllProductsUseCase
import com.soictnative.ardecor.domain.usecases.product.SearchProductsUseCase
import com.soictnative.ardecor.util.NetworkResponseState
import com.soictnative.ardecor.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FurnitureInventoryViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val categoryUseCase: CategoryUseCase,
    private val searchProductsUseCase: SearchProductsUseCase
): ViewModel() {

    private val _products = MutableStateFlow<ScreenState<List<Product>>>(ScreenState.Unspecified)
    val products: StateFlow<ScreenState<List<Product>>> get() = _products

    private val _categories = MutableStateFlow<ScreenState<List<Category>>>(ScreenState.Unspecified)
    val categories: StateFlow<ScreenState<List<Category>>> get() = _categories

    init {
        fetchInventoryProducts()
        fetchCategories()
    }

    fun fetchProductOnCategory(categoryId: Int) {
        searchProductsUseCase(
            categoryId = categoryId
        ).onEach {
            when (it) {
                is NetworkResponseState.Loading -> _products.emit(ScreenState.Loading)
                is NetworkResponseState.Success -> _products.emit(ScreenState.Success(it.result.data))
                is NetworkResponseState.Error -> _products.emit(ScreenState.Error(it.exception.message!!))
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

    private fun fetchInventoryProducts() {
        getAllProductsUseCase().onEach {
            when(it) {
                is NetworkResponseState.Loading -> _products.emit(ScreenState.Loading)
                is NetworkResponseState.Success -> _products.emit(ScreenState.Success(it.result.data))
                is NetworkResponseState.Error -> _products.emit(ScreenState.Error(it.exception.message!!))
            }
        }.launchIn(viewModelScope)
    }
}