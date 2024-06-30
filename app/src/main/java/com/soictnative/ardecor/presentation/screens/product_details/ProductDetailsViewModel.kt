package com.soictnative.ardecor.presentation.screens.product_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.data.entity.FavouriteProductEntity
import com.soictnative.ardecor.data.entity.RecentlyViewedProductEntity
import com.soictnative.ardecor.domain.usecases.favourite.InsertFavouriteProductUseCase
import com.soictnative.ardecor.domain.usecases.product.GetSingleProductUseCase
import com.soictnative.ardecor.domain.usecases.recently_viewed.GetRecentlyViewedProductUseCase
import com.soictnative.ardecor.domain.usecases.recently_viewed.InsertRecentlyViewedProductUseCase
import com.soictnative.ardecor.util.NetworkResponseState
import com.soictnative.ardecor.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getSingleProductUseCase: GetSingleProductUseCase,
    private val insertRecentlyViewedProductUseCase: InsertRecentlyViewedProductUseCase,
    private val insertFavouriteProductUseCase: InsertFavouriteProductUseCase,
    private val getRecentlyViewedProductUseCase: GetRecentlyViewedProductUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val storageRef: StorageReference,
) : ViewModel() {

    private val _product = MutableLiveData<ScreenState<Product>>()
    val product: LiveData<ScreenState<Product>> get() = _product

    private val _recentlyViewedProducts = MutableLiveData<ScreenState<List<RecentlyViewedProductEntity>>>()
    val recentlyViewedProducts: LiveData<ScreenState<List<RecentlyViewedProductEntity>>> get() = _recentlyViewedProducts

    private val _userUid = MutableLiveData<String>()
    val userUid: LiveData<String> get() = _userUid

    init {
        getProduct()
    }

    private fun getProduct() {
        viewModelScope.launch {
            savedStateHandle.get<Int>("productId")?.let { productId ->
                getSingleProductUseCase(productId).collect {
                    when(it) {
                        is NetworkResponseState.Error -> _product.postValue(ScreenState.Error(it.exception.message!!))
                        is NetworkResponseState.Loading -> _product.postValue(ScreenState.Loading)
                        is NetworkResponseState.Success -> {
                            val productSuccessState = it.result.data
                            _product.postValue(ScreenState.Success(it.result.data))
                            firebaseAuth.currentUser?.let {
                                _userUid.postValue(it.uid)
                                insertRecentlyViewedProductUseCase(
                                    RecentlyViewedProductEntity(
                                        id = productSuccessState.id,
                                        name = productSuccessState.name,
                                        price = productSuccessState.price,
                                        modelPath = productSuccessState.model_path!!,
                                        isStackable = productSuccessState.is_stackable == 1,
                                        source = productSuccessState.source,
                                        description = productSuccessState.description,
                                        imageUrl = productSuccessState.image_url,
                                        categoryId = productSuccessState.category_id,
                                        lastAccessedTime = System.currentTimeMillis(),
                                        userUid = it.uid,
                                    )
                                )
                                getFavouriteProducts(it.uid)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getFavouriteProducts(userUid: String) {
        viewModelScope.launch {
            getRecentlyViewedProductUseCase(userUid).collectLatest {
                when(it) {
                    is NetworkResponseState.Loading -> _recentlyViewedProducts.postValue(ScreenState.Loading)
                    is NetworkResponseState.Error -> _recentlyViewedProducts.postValue(ScreenState.Error(it.exception.message!!))
                    is NetworkResponseState.Success -> _recentlyViewedProducts.postValue(ScreenState.Success(it.result))
                }
            }
        }
    }

    fun switchProduct(productId: Int) {
        getSingleProductUseCase(productId).onEach {
            when(it) {
                is NetworkResponseState.Error -> _product.postValue(ScreenState.Error(it.exception.message!!))
                is NetworkResponseState.Loading -> _product.postValue(ScreenState.Loading)
                is NetworkResponseState.Success -> _product.postValue(ScreenState.Success(it.result.data))
            }
        }.launchIn(viewModelScope)
    }

    fun getDownloadUrl(
        fileReference: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        storageRef.child(fileReference).downloadUrl
            .addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
            .addOnFailureListener(onFailure)
    }

    fun addProductToFavourite(product: Product) {
        firebaseAuth.currentUser?.let {
            val favouriteProduct = FavouriteProductEntity(
                id = product.id,
                name = product.name,
                price = product.price,
                modelPath = product.model_path!!,
                isStackable = product.is_stackable == 1,
                source = product.source,
                description = product.description,
                imageUrl = product.image_url,
                categoryId = product.category_id,
                createdAt = System.currentTimeMillis(),
                userUid = it.uid,
            )
            viewModelScope.launch {
                insertFavouriteProductUseCase(favouriteProduct)
            }
        }
    }
}