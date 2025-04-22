package com.example.frontend.ui.screen.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel()
) {

    val cart by cartViewModel.cart

    Text(text = "${cart?.cartId}")

    Card(){
        Text(
            text = "Don’t let your cart catch dust—check it out!",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            fontWeight = FontWeight.W900,
        )
    }

    Row(){
//        CloudinaryImage()
        Column(){}
        Column(){}
    }
}






