package com.example.frontend.ui.screen.login

import androidx.lifecycle.ViewModel
import com.example.frontend.data.repository.LoginRepository
import javax.inject.Inject
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.LoginData
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.repository.CartRepository
import com.example.frontend.ui.common.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false
)

data class LoginTextFieldUiState(
    val userName: String? = "",
    val password: String? = "",
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepo: LoginRepository,
    private val authManager: AuthManager,
    private val cartRepo: CartRepository,
    ): ViewModel() {

    private val _loginState =  mutableStateOf<LoginUiState>(LoginUiState())
    val loginState: State<LoginUiState> = _loginState

    private val _loginTextFieldState = mutableStateOf<LoginTextFieldUiState>(LoginTextFieldUiState())
    val loginTextFieldState: State<LoginTextFieldUiState> = _loginTextFieldState

    private val _isInitialCheckDone = mutableStateOf<Boolean>(false)
    val isInitialCheckDone: State<Boolean> = _isInitialCheckDone

    private val _isLoggedIn = mutableStateOf<Boolean>(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn

    init {
        checkInitialLoginStatus()
    }

    fun onUserNameChange(userName: String){
        _loginTextFieldState.value = _loginTextFieldState.value.copy(userName = userName)
    }
    fun onPasswordChange(password: String){
        _loginTextFieldState.value = _loginTextFieldState.value.copy(password = password)
    }

    fun setLoginTextField(userName: String, password: String){
        _loginTextFieldState.value = _loginTextFieldState.value.copy(userName = userName, password = password)
    }

    private fun checkInitialLoginStatus() {
        viewModelScope.launch {
            _isLoggedIn.value = authManager.isLoggedInOnce()
            _isInitialCheckDone.value = true
        }
    }
    fun loginWithGoogle(idToken: String) {
        _loginState.value = LoginUiState(isLoading = true)
        viewModelScope.launch {
            when (val result = loginRepo.loginWithGoogle(idToken)) {
                is ApiResponse.Success -> {
                    // Lưu cả token và userName
                    val data = result.data
                    authManager.saveLoginStatus(
                        isLoggedIn = true,
                        userName   = data.userName,
                        cartId     = null,
                        authToken  = data.token
                    )
                    _isLoggedIn.value = true
                    _loginState.value = LoginUiState(loginSuccess = true)
                }
                is ApiResponse.Error -> {
                    _loginState.value = LoginUiState(
                        errorMessage = result.message,
                        isLoading    = false
                    )
                }
                else -> {  }
            }
        }
    }
    fun login(user: LoginData){
        _loginState.value = LoginUiState(isLoading = true)

        viewModelScope.launch{
         val result = loginRepo.login(user)
            _loginState.value = when(result){
                is ApiResponse.Success -> {
                    authManager.saveLoginStatus(true, user.userName)
                    _isLoggedIn.value= true
                    LoginUiState(loginSuccess = true)
                }
                is ApiResponse.Error -> LoginUiState(errorMessage = result.message, isLoading = false)
                is ApiResponse.Loading -> LoginUiState(isLoading = true)
            }
        }
    }

    fun logout(){
        viewModelScope.launch{
            _isLoggedIn.value = false
            _loginState.value = LoginUiState(loginSuccess = false)
            authManager.clearLoginStatus()
        }
    }

}


