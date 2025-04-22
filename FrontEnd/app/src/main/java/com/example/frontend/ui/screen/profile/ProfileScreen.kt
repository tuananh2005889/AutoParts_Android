package com.example.frontend.ui.screen.profile

import android.graphics.Paint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    modifier: Modifier = Modifier,
    onLogout: ()->Unit
){
    val userNameState by profileViewModel.userNameState.collectAsState()

    Column (
        modifier.fillMaxSize(),
    ){
        Text(text = "User Name: ${userNameState}")
        Button(
            onClick = {onLogout()},
        ){
            Text(text = "Logout")
        }
    }

}

