package com.example.frontend.ui.screen.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.data.dto.CartItemDTO
import com.example.frontend.ui.common.CloudinaryImage

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel()
) {

    val cart by cartViewModel.cart
    val cartItems by cartViewModel.cartItemDTOs


    Column(){
        Row{
            Card(){

                Text(
                    text = "Don’t let your cart catch dust—check it out!",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.W900,
                )

            }
        }

        CartItemsList(
            cartViewModel = cartViewModel,
            cartItemDTOs = cartItems,)

    }

}
@Composable
fun CartItemsList(
    cartViewModel: CartViewModel,
    cartItemDTOs: List<CartItemDTO>,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items = cartItemDTOs) { cartItemDTO ->
            val imageUrl = cartViewModel.imageUrlMap[cartItemDTO.productId]
            CartItemRow(
                cartItemDTO =  cartItemDTO,
                imageUrl = imageUrl ?: "",
            )
        }
    }
}
@Composable
fun CartItemRow(
    modifier: Modifier = Modifier,
    cartItemDTO: CartItemDTO,
    imageUrl: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CloudinaryImage(
            url = imageUrl,
            contentDescription = "Product Image",
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .weight(2f)
                .padding(horizontal = 8.dp)
        ) {
            Text(text = cartItemDTO.productName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = cartItemDTO.quantity.toString(), color = Color.Gray)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {},
            modifier = Modifier.weight(0.5f)
        ) {
            Text("Xoá")
        }
    }
}







