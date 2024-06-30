package com.soictnative.ardecor.presentation.screens.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.soictnative.ardecor.data.dto.User
import com.soictnative.ardecor.domain.usecases.recently_viewed.ClearRecentlyViewedProductUseCase
import com.soictnative.ardecor.util.Constants
import com.soictnative.ardecor.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val clearRecentlyViewedProductUseCase: ClearRecentlyViewedProductUseCase,
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {

    private val _user = MutableLiveData<ScreenState<User>>()
    val user: LiveData<ScreenState<User>> get() = _user

    init {
        getUserInfo()
    }

    fun getUserInfo() {
        firebaseAuth.currentUser?.let {
            viewModelScope.launch {
                _user.postValue(ScreenState.Loading)
            }
            db.collection(Constants.USER_COLLECTION)
                .document(it.uid)
                .get()
                .addOnSuccessListener { document ->
                    val user = document.toObject<User>()
                    if (user != null) {
                        viewModelScope.launch {
                            _user.postValue(ScreenState.Success(user))
                        }
                    } else {
                        viewModelScope.launch {
                            _user.postValue(ScreenState.Error("User is null"))
                        }
                    }
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _user.postValue(ScreenState.Error(it.message.toString()))
                    }
                }
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        viewModelScope.launch {
            firebaseAuth.currentUser?.let {
                clearRecentlyViewedProductUseCase(it.uid)
            }
        }
    }
}