package com.example.frontend.ViewModel
import androidx.lifecycle.ViewModel
import com.example.frontend.data.model.LoginData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.frontend.data.model.UserData

class UserViewModel : ViewModel() {
    private val _currentUser = MutableStateFlow<LoginData?>(null)
    val currentUser: StateFlow<LoginData?> = _currentUser

    fun setCurrentUser(user: LoginData) {
        _currentUser.value = user
    }
}