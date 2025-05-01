package com.example.frontend.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.UserData
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepo: UserRepository
) : ViewModel() {

    private val _userDataState = MutableStateFlow<UserData?>(null)
    val userDataState: StateFlow<UserData?> = _userDataState.asStateFlow()

    private val _updateAvatarError = MutableStateFlow<String?>(null)
    val updateAvatarError: StateFlow<String?> = _updateAvatarError.asStateFlow()

    /*--- Lấy info ---*/
    fun loadUser(userName: String) = viewModelScope.launch {
        _userDataState.value = null     // show loading spinner

        when (val res = userRepo.getUser(userName)) {
            is ApiResponse.Success -> _userDataState.value = res.data
            is ApiResponse.Error   -> {
                Log.e("ProfileVM", "loadUser: ${res.message}")
                _updateAvatarError.value = res.message
            }
            ApiResponse.Loading    -> {}
        }
    }

    /*--- Update avatar ---*/
    fun updateAvatarUrl(userName: String, avatarUrl: String) = viewModelScope.launch {
        when (val res = userRepo.updateAvatar(userName, avatarUrl)) {
            is ApiResponse.Success -> _userDataState.update { it?.copy(avatarUrl = avatarUrl) }
            is ApiResponse.Error   -> _updateAvatarError.value = res.message
            ApiResponse.Loading    -> {}
        }
    }

    fun clearAvatarError() { _updateAvatarError.value = null }
}