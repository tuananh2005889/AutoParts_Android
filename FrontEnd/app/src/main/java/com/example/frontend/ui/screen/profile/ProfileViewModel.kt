package com.example.frontend.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.ui.common.AuthManager
import com.example.frontend.ui.common.AuthPreferencesKeys.userName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authManager: AuthManager
): ViewModel() {
    private val _userNameState = MutableStateFlow<String?>(null)
    val userNameState: StateFlow<String?> = _userNameState.asStateFlow()

    init{
        viewModelScope.launch {
           authManager.userNameFlow.collect{ userName ->
               _userNameState.value = userName
           }
        }
    }
}