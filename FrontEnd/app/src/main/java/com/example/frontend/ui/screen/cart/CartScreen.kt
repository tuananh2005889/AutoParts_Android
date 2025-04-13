package com.example.frontend.ui.screen.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CartScreen(modifier: Modifier = Modifier){
    Box(
        modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Cart Sreen")
    }
}