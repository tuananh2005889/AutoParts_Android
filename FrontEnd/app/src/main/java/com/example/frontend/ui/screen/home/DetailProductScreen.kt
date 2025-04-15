package com.example.frontend.ui.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DetailProductScreen(
    productId: String

){
    Box(modifier = Modifier.fillMaxSize()){
        Text(text="detail", fontWeight = FontWeight(500))
    }
}