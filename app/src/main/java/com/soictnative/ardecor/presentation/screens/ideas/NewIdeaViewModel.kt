package com.soictnative.ardecor.presentation.screens.ideas

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import com.soictnative.ardecor.data.dto.Idea
import com.soictnative.ardecor.data.dto.IdeaImage
import com.soictnative.ardecor.data.dto.RoomType
import com.soictnative.ardecor.data.dto.SavedPlacement
import com.soictnative.ardecor.domain.usecases.ideas.CreateNewIdeaUseCase
import com.soictnative.ardecor.domain.usecases.room_types.GetAllRoomTypesUseCase
import com.soictnative.ardecor.util.NetworkResponseState
import com.soictnative.ardecor.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NewIdeaViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val storageReference: StorageReference,
    private val getAllRoomTypesUseCase: GetAllRoomTypesUseCase,
    private val createNewIdeaUseCase: CreateNewIdeaUseCase
) : ViewModel() {

    private val _newIdea = MutableLiveData<ScreenState<Idea>>()
    val newIdea: LiveData<ScreenState<Idea>> get() = _newIdea

    private val _roomTypes = MutableLiveData<ScreenState<List<RoomType>>>()
    val roomTypes : LiveData<ScreenState<List<RoomType>>> get() = _roomTypes

    init {
        getallRoomTypes()
    }

    private fun getallRoomTypes() {
        getAllRoomTypesUseCase().onEach {
            when(it) {
                is NetworkResponseState.Error -> _roomTypes.postValue(ScreenState.Error(it.exception.message!!))
                is NetworkResponseState.Loading -> _roomTypes.postValue(ScreenState.Loading)
                is NetworkResponseState.Success -> _roomTypes.postValue(ScreenState.Success(it.result.data))
            }
        }.launchIn(viewModelScope)
    }

    suspend fun uploadImagesToFirebase(uris: List<Uri>): List<String> {
        val urls = mutableListOf<String>()

        uris.forEach { uri ->
            val storageRef = storageReference.child("images/${UUID.randomUUID()}")
            val uploadTask = storageRef.putFile(uri).await()
            val downloadUrl = storageRef.downloadUrl.await()
            urls.add(downloadUrl.toString())
        }

        return urls
    }

    fun onSubmit(name: String, description: String, roomTypeId: Int, uris: List<Uri>, savedPlacement: SavedPlacement) {
        val user = firebaseAuth.currentUser ?: return
        viewModelScope.launch {
            val imageUrls = uploadImagesToFirebase(uris)
            val ideaImages = imageUrls.map {
                IdeaImage(0,0, it, "", "")
            }
            val newIdeaBody = Idea(
                id = 0,
                name = name,
                description = description,
                placements = savedPlacement.placements,
                image_url = imageUrls[0],
                room_type_id = roomTypeId,
                user_id = user.uid,
                created_at = "",
                updated_at = "",
                images = ideaImages
            )
            createNewIdeaUseCase(newIdeaBody).collect {
                when(it) {
                    is NetworkResponseState.Loading -> _newIdea.postValue(ScreenState.Loading)
                    is NetworkResponseState.Error -> _newIdea.postValue(ScreenState.Error(it.exception.message!!))
                    is NetworkResponseState.Success -> _newIdea.postValue(ScreenState.Success(it.result.data))
                }
            }
        }
    }
}