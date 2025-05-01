package com.example.frontend.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordVM @Inject constructor(private val repo: UserRepository): ViewModel() {
    var email by mutableStateOf("")
    var code by mutableStateOf("")
    var newPass by mutableStateOf("")
    var step by mutableIntStateOf(1)    // 1=nhập email, 2=nhập code, 3=nhập pass
    var loading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun onEmailChange(v: String) { email = v }
    fun onCodeChange(v: String) { code = v }
    fun onNewPassChange(v: String) { newPass = v }

    fun sendCode() = viewModelScope.launch {
        loading = true; error = null
        try {
            val resp = repo.sendResetCode(email)
            if (resp.isSuccessful) {
                step = 2
            } else {
                error = resp.errorBody()?.string() ?: "Email not found (${resp.code()})"
            }
        } catch(e: Exception) {
            error = "Server errol"
        }
        loading = false
    }

    fun verifyCode() = viewModelScope.launch {
        loading = true; error = null
        try {
            if (repo.checkCode(email, code) != null) step = 3
            else error = "Code invalid"
        } catch(e: Exception) {
            error = "Fail to verify code"
        }
        loading = false
    }
    fun resetPassword(onDone: ()->Unit) = viewModelScope.launch {
        loading = true; error = null
        try {
            val resp = repo.changePassword(email, newPass)
            if (resp.isSuccessful) {
                onDone()
            } else {
                // đọc message server trả về hoặc code HTTP
                error = resp.errorBody()?.string() ?: "Change password failed (${resp.code()})"
            }
        } catch(e: Exception) {
            error = "Server errol"
        }
        loading = false
    }

}