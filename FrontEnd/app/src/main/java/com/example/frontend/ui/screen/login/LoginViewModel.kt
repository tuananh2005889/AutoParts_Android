package com.example.frontend.ui.screen.login

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.LoginData
import com.example.frontend.data.model.UserData
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.remote.GoogleLoginResponse
import com.example.frontend.data.repository.LoginRepository
import com.example.frontend.ui.common.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/* ---------- UI state ---------- */
data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false
)
data class LoginTextFieldUiState(
    val userName: String = "",
    val password: String = "",
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepo: LoginRepository,
    private val authManager: AuthManager
) : ViewModel() {


    /* ---------- Compose states ---------- */
    private val _ui   = mutableStateOf(LoginUiState())
    val ui: State<LoginUiState> = _ui

    private val _text = mutableStateOf(LoginTextFieldUiState())
    val text: State<LoginTextFieldUiState> = _text

    /* ---------- Global flags ---------- */
    private val _isLoggedIn      = mutableStateOf(false)
    private val _isInitDone      = mutableStateOf(false)

    /** Đọc trạng thái login sẵn có trong DataStore */
    init { viewModelScope.launch {
        _isLoggedIn.value = authManager.isLoggedInOnce()
        _isInitDone.value = true
    }}

    /* ---------- Text change ---------- */
    fun onUserNameChange(u: String) { _text.value = _text.value.copy(userName = u) }
    fun onPasswordChange(p: String) { _text.value = _text.value.copy(password = p) }

    /* ---------- Google login ---------- */
    fun loginWithGoogle(idToken: String, avatarUrl: String?) {
        _ui.value = LoginUiState(isLoading = true)
        viewModelScope.launch {
            when (val res = loginRepo.loginWithGoogle(idToken)) {
                is ApiResponse.Success -> {
                    val g: GoogleLoginResponse = res.data

                    authManager.saveLoginStatus(true, g.userName, null, g.token)
                    authManager.saveUserData(
                        UserData(
                            userId    = g.userId,
                            userName  = g.userName,
                            fullName  = g.userName,
                            gmail     = "",
                            phone     = "",
                            password  = "",
                            avatarUrl = avatarUrl
                        )
                    )

                    _isLoggedIn.value = true
                    _ui.value = LoginUiState(loginSuccess = true)
                }
                is ApiResponse.Error ->
                    _ui.value = LoginUiState(errorMessage = res.message, isLoading = false)
                ApiResponse.Loading -> {}
            }
        }
    }


    /* ---------- Username / Password login ---------- */
    fun login(user: LoginData) {
        _ui.value = LoginUiState(isLoading = true)
        viewModelScope.launch {
            when (val res = loginRepo.login(user)) {
                is ApiResponse.Success -> {
                    val (token, userData) = res.data   // Pair<String, UserData?>
                    authManager.saveLoginStatus(true, user.userName, null, token)
                    userData?.let { authManager.saveUserData(it) }

                    _isLoggedIn.value = true
                    _ui.value = LoginUiState(loginSuccess = true)
                }
                is ApiResponse.Error ->
                    _ui.value = LoginUiState(errorMessage = res.message, isLoading = false)
                ApiResponse.Loading -> {}
            }
        }
    }

    /* ---------- Logout ---------- */
    fun logout() = viewModelScope.launch {
        authManager.clearLoginStatus()
        _isLoggedIn.value = false
        _ui.value = LoginUiState(loginSuccess = false)
    }

    /* ---------- Helpers cho Navigation ---------- */
    val isLoggedIn: State<Boolean>         get() = _isLoggedIn
    val isInitialCheckDone: State<Boolean> get() = _isInitDone

    suspend fun getUserName(): String? = authManager.getUserNameOnce()

    fun setLoginTextField(userName: String = "", password: String = "") {
        _text.value = LoginTextFieldUiState(userName, password)
    }
}
