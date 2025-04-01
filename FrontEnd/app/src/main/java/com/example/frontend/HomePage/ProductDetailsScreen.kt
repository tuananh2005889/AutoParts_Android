package com.example.frontend.HomePage


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ProductDetailsScreen(navController: NavHostController, productId: Long?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Product Details")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Product ID: $productId")
    }
}