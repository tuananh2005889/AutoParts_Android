package com.example.frontend.ui.screen.cart

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.ViewModel.CartViewModel
import com.example.frontend.data.dto.CartItemDTO
import com.example.frontend.ui.common.CloudinaryImage
import com.example.frontend.ui.common.Notification
import kotlinx.coroutines.delay

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val cartItems by cartViewModel.cartItemDTOList

    val errorMessage by cartViewModel.errorMessage

    var visible  by remember {  mutableStateOf(false) }

    val imageUrls by cartViewModel.imageUrlPerCartItemList


    val cartId by cartViewModel.cartId.collectAsState()

    LaunchedEffect(errorMessage){
        if(errorMessage != null){
            visible = true
            delay(2000L)
            visible = false
        }
    }

    Box{
        Log.d("cartscreen-imageUrls", imageUrls.toString())
        Column{
                Card(
                    modifier = Modifier.padding(horizontal = 8.dp , vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceTint
                    )
                ){

                    Text(
                        text = "Don’t let your cart catch dust—check it out! $cartId",
                        modifier = Modifier.padding(horizontal = 16.dp,
                            vertical = 8.dp),
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        fontWeight = FontWeight.W900,
                    )

                }


            CartItemsList(
                imageUrls = imageUrls,
                cartViewModel = cartViewModel,
                cartItemDTOs = cartItems,)
        }

        if(visible){
            Notification(
                modifier = Modifier
                    .align(Alignment.TopCenter )
                ,
                text = errorMessage ?: "Unknown error"
            )
        }
    }

}
@Composable
fun CartItemsList(
    cartViewModel: CartViewModel,
    cartItemDTOs: List<CartItemDTO>,
    imageUrls: List<String>,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(items = cartItemDTOs) {index, cartItemDTO ->

            val imageUrl = imageUrls.getOrNull(index) ?: "null"
            Log.d("CartScreen-imageurl", imageUrl)
            CartItemRow(
                cartItemDTO =  cartItemDTO,
                imageUrl = imageUrl,
                onClickDecrease = {cartViewModel.decreaseQuantity(index.toLong() + 1)},
                onClickIncrease = {cartViewModel.increaseQuantity(index.toLong() + 1)},
            )
        }
    }
}
@Composable
fun CartItemRow(
    modifier: Modifier = Modifier,
    cartItemDTO: CartItemDTO,
    imageUrl: String,
    onClickDecrease: ()->Unit,
    onClickIncrease: ()->Unit,
) {
    Card(
        modifier =  Modifier
        .padding(horizontal = 12.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()

            ,
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
                    .weight(1.75f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = cartItemDTO.productName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = cartItemDTO.quantity.toString(), color = Color.Gray)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row(
                modifier = Modifier.weight(0.75f)
            ) {
                Button (
                    modifier = Modifier.size(32.dp),
                    contentPadding = PaddingValues(0.dp),
                    onClick = {onClickIncrease()}
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = {onClickDecrease()},
                    modifier = Modifier.size(32.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(15.dp),
                        painter = painterResource(id = R.drawable.minus_sign),
                        contentDescription = null
                    )
                }
            }
        }
    }
}







