package com.soictnative.ardecor.presentation.screens.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.soictnative.ardecor.data.entity.FavouriteProductEntity
import com.soictnative.ardecor.domain.usecases.favourite.DeleteFavouriteProductUseCase
import com.soictnative.ardecor.domain.usecases.favourite.GetFavouriteProductsUseCase
import com.soictnative.ardecor.util.NetworkResponseState
import com.soictnative.ardecor.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val getFavouriteProductsUseCase: GetFavouriteProductsUseCase,
    private val deleteFavouriteProductUseCase: DeleteFavouriteProductUseCase
) : ViewModel() {

    private val _favouriteProducts = MutableLiveData<ScreenState<List<FavouriteProductEntity>>>()
    val favouriteProducts: LiveData<ScreenState<List<FavouriteProductEntity>>> get() = _favouriteProducts

    init {
        getFavouriteProducts()
    }

    fun refresh() {
        getFavouriteProducts()
    }

    private fun getFavouriteProducts() {
        viewModelScope.launch {
            firebaseAuth.currentUser?.let {
                getFavouriteProductsUseCase(it.uid).collect {
                    when(it) {
                        is NetworkResponseState.Loading -> _favouriteProducts.postValue(ScreenState.Loading)
                        is NetworkResponseState.Error -> _favouriteProducts.postValue(ScreenState.Error(it.exception.message!!))
                        is NetworkResponseState.Success -> _favouriteProducts.postValue(ScreenState.Success(it.result))
                    }
                }
            }
        }
    }

    fun removeFromFavouriteProducts(product: FavouriteProductEntity) {
        if (_favouriteProducts.value is ScreenState.Success) {
            val newList = (_favouriteProducts.value as ScreenState.Success<List<FavouriteProductEntity>>).uiData
            val filteredList = newList.filter { it.id != product.id }
            _favouriteProducts.postValue(ScreenState.Success(filteredList))
        }
        viewModelScope.launch {
            deleteFavouriteProductUseCase(product)
        }
    }
}