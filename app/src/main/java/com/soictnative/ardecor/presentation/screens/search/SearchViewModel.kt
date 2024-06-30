package com.soictnative.ardecor.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.domain.usecases.product.SearchProductsUseCase
import com.soictnative.ardecor.util.NetworkResponseState
import com.soictnative.ardecor.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase,
) : ViewModel() {

    private val _searchText = MutableStateFlow( "")
    val searchText = _searchText.asStateFlow()

    private var _products = MutableStateFlow<ScreenState<List<Product>>>(ScreenState.Unspecified)
    val products: StateFlow<ScreenState<List<Product>>> get() = _products.asStateFlow()

    private val searchParamsChannel = Channel<SearchParameter>(capacity = Channel.CONFLATED)

    private val _sort = MutableStateFlow<String?>(null)

    private val _minPrice = MutableStateFlow<Float?>(null)

    private val _maxPrice = MutableStateFlow<Float?>(null)

    val sort: StateFlow<String?> get() = _sort.asStateFlow()

    val minPrice: StateFlow<Float?> get() = _minPrice.asStateFlow()

    val maxPrice: StateFlow<Float?> get() = _maxPrice.asStateFlow()

    init {
        onSearchTextChanged("")
    }

    fun onSearchTextChanged(text: String) {
        _searchText.value = text
        updateSearchParams()
    }

    fun onSortChanged(text: String?) {
        _sort.value = text
        updateSearchParams()
    }

    fun onPriceRangeChosen(minPrice: Float?, maxPrice: Float?) {
        _minPrice.value = minPrice
        _maxPrice.value = maxPrice
        updateSearchParams()
    }

    private fun updateSearchParams() {
        viewModelScope.launch {
            searchParamsChannel.send(
                SearchParameter(
                    _searchText.value,
                    _sort.value,
                    _minPrice.value,
                    _maxPrice.value
                )
            )
            delay(1000 )
            val latestParams = try {
                searchParamsChannel.receive()
            } catch (e: CancellationException) {
                return@launch
            }
            searchProducts(latestParams)
        }
    }
    private fun searchProducts(searchParams: SearchParameter) {
        searchProductsUseCase(
            keyword = searchParams.searchText,
            sortBy = searchParams.sortBy,
            minPrice = searchParams.minPrice,
            maxPrice = searchParams.maxPrice,
        ).onEach {
            when(it) {
                is NetworkResponseState.Loading -> _products.emit(ScreenState.Loading)
                is NetworkResponseState.Success -> _products.emit(ScreenState.Success(it.result.data))
                is NetworkResponseState.Error -> _products.emit(ScreenState.Error(it.exception.message!!))
            }
        }.launchIn(viewModelScope)
    }
}

data class SearchParameter(
    val searchText: String,
    val sortBy: String?,
    val minPrice: Float?,
    val maxPrice: Float?,
)