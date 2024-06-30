package com.soictnative.ardecor.presentation.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.soictnative.ardecor.data.dto.User
import com.soictnative.ardecor.util.SignInResult
import com.soictnative.ardecor.util.Constants
import com.soictnative.ardecor.util.Constants.PREF_FIREBASE_USERID_KEY
import com.soictnative.ardecor.util.Constants.USER_TOKEN
import com.soictnative.ardecor.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login get() = _login.asSharedFlow()

    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword get() = _resetPassword.asSharedFlow()

    fun login(email: String, password: String)
    {
        viewModelScope.launch { _login.emit(Resource.Loading()) }
        firebaseAuth.signInWithEmailAndPassword(
            email, password
        ).addOnSuccessListener {
            it.user?.let {
                sharedPreferences.edit()
                    .putString(PREF_FIREBASE_USERID_KEY, it.uid)
                    .apply()
                it.getIdToken(false).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sharedPreferences.edit()
                            .putString(USER_TOKEN, task.result?.token)
                            .apply()
                    }
                }
            }
            viewModelScope.launch {
                it.user?.let {
                    _login.emit(Resource.Success(it))
                }
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _login.emit(Resource.Error(it.message.toString()))
            }
        }
    }

    fun onSignInResult(result: SignInResult) {
        result.firebaseUser?.let {
            sharedPreferences.edit()
                .putString(PREF_FIREBASE_USERID_KEY, result.firebaseUser.uid)
                .apply()
            it.getIdToken(false).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sharedPreferences.edit()
                        .putString(USER_TOKEN, task.result?.token)
                        .apply()
                }
            }
            val newUser = result.displayName?.let { displayName ->
                User(
                    displayName,
                    it.email!!,
                    result.photoUrl
                )
            }
            saveUserInfo(it.uid, newUser!!, it)
        }
    }

    private fun saveUserInfo(userUid: String, user: User, firebaseUser: FirebaseUser) {
        db.collection(Constants.USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _login.emit(Resource.Success(firebaseUser))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _login.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _resetPassword.emit(Resource.Loading())
        }

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _resetPassword.emit(Resource.Success(email))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _resetPassword.emit(Resource.Error(it.message.toString()))
                }
        }
    }
}