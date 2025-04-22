package com.example.frontend.ui.screen.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.data.repository.CartRepository

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel()
) {

    val cart by cartViewModel.cart

    Text(text = "${cart?.cartId}")
   }






