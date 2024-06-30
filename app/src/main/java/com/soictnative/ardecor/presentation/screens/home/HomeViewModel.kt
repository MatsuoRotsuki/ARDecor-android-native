package com.soictnative.ardecor.presentation.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.google.firebase.auth.FirebaseAuth
import com.soictnative.ardecor.data.dto.Category
import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.data.entity.RecentlyViewedProductEntity
import com.soictnative.ardecor.domain.usecases.category.CategoryUseCase
import com.soictnative.ardecor.domain.usecases.product.GetAllPagingProductsUseCase
import com.soictnative.ardecor.domain.usecases.product.GetAllProductsUseCase
import com.soictnative.ardecor.domain.usecases.recently_viewed.GetRecentlyViewedProductUseCase
import com.soictnative.ardecor.util.NetworkResponseState
import com.soictnative.ardecor.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val categoryUseCase: CategoryUseCase,
    private val getAllPagingProductsUseCase: GetAllPagingProductsUseCase,
    private val getRecentlyViewedProductUseCase: GetRecentlyViewedProductUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _products = MutableLiveData<ScreenState<List<Product>>>()
    val products: LiveData<ScreenState<List<Product>>> get() = _products

    private val _recentlyViewedProducts = MutableLiveData<ScreenState<List<RecentlyViewedProductEntity>>>()
    val recentlyViewedProducts: LiveData<ScreenState<List<RecentlyViewedProductEntity>>> get() = _recentlyViewedProducts

    private val _categories = MutableLiveData<ScreenState<List<Category>>>()
    val categories: LiveData<ScreenState<List<Category>>> get() = _categories

    private val _login = MutableSharedFlow<Boolean>()
    val login: SharedFlow<Boolean> get() = _login

    private val _userUid = MutableLiveData<String?>()
    val userUid: LiveData<String?> get() = _userUid

    //
    val pagingProducts = getAllPagingProductsUseCase().cachedIn(viewModelScope)
    //
    init {
        getAllCategories()
        getAllProducts()

        val user = firebaseAuth.currentUser
        if (user != null) {
            viewModelScope.launch {
                _login.emit(true)
                _userUid.postValue(user.uid)
                getFavouriteProducts(user.uid)
            }
        } else {
            viewModelScope.launch {
                _login.emit(false)
            }
        }
    }

    fun refresh() {
        getAllProducts()

        getAllCategories()
        val user = firebaseAuth.currentUser
        if (user != null) {
            viewModelScope.launch {
                _login.emit(true)
                _userUid.postValue(user.uid)
                getFavouriteProducts(user.uid)
            }
        } else {
            viewModelScope.launch {
                _login.emit(false)
            }
        }
    }

    private fun getAllProducts() {
        getAllProductsUseCase().onEach {
            when(it) {
                is NetworkResponseState.Error -> _products.postValue(ScreenState.Error(it.exception.message!!))
                is NetworkResponseState.Loading -> _products.postValue(ScreenState.Loading)
                is NetworkResponseState.Success -> _products.postValue(ScreenState.Success(it.result.data))
            }
        }.launchIn(viewModelScope)
    }

    private fun getFavouriteProducts(userUid: String) {
        viewModelScope.launch {
            getRecentlyViewedProductUseCase.invoke(userUid).collect {
                when(it) {
                    is NetworkResponseState.Loading -> _recentlyViewedProducts.postValue(ScreenState.Loading)
                    is NetworkResponseState.Error -> _recentlyViewedProducts.postValue(ScreenState.Error(it.exception.message!!))
                    is NetworkResponseState.Success -> _recentlyViewedProducts.postValue(ScreenState.Success(it.result))
                }
            }
        }
    }

    private fun getAllCategories() {
        categoryUseCase().onEach {
            when(it) {
                is NetworkResponseState.Error -> _categories.postValue(ScreenState.Error(it.exception.message!!))
                is NetworkResponseState.Loading -> _categories.postValue(ScreenState.Loading)
                is NetworkResponseState.Success -> _categories.postValue(ScreenState.Success(it.result.data))
            }
        }.launchIn(viewModelScope)
    }
}