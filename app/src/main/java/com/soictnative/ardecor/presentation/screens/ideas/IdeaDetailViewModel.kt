package com.soictnative.ardecor.presentation.screens.ideas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.soictnative.ardecor.data.dto.Idea
import com.soictnative.ardecor.domain.usecases.ideas.GetSingleIdeaUseCase
import com.soictnative.ardecor.util.NetworkResponseState
import com.soictnative.ardecor.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IdeaDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getSingleIdeaUseCase: GetSingleIdeaUseCase,
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {
    
    private val _idea = MutableLiveData<ScreenState<Idea>>()
    val idea: LiveData<ScreenState<Idea>> get() = _idea

    private val _userUid = MutableLiveData<String>()
    val userUid: LiveData<String> get() = _userUid

    init {
        getIdea()
        getUserUid()
    }

    private fun getIdea() {
        viewModelScope.launch {
            savedStateHandle.get<Int>("ideaId")?.let { ideaId ->
                getSingleIdeaUseCase(ideaId).collect {
                    when(it) {
                        is NetworkResponseState.Error -> _idea.postValue(ScreenState.Error(it.exception.message!!))
                        is NetworkResponseState.Loading -> _idea.postValue(ScreenState.Loading)
                        is NetworkResponseState.Success -> _idea.postValue(ScreenState.Success(it.result.data))
                    }
                }
            }
        }
    }

    private fun getUserUid() {
        firebaseAuth.currentUser?.let {
            _userUid.postValue(it.uid)
        }
    }
}