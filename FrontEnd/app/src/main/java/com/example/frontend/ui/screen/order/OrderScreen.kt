package com.example.frontend.ui.screen.order

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.ViewModel.OrderViewModel
import com.example.frontend.data.dto.CartItemDTO
import com.example.frontend.data.dto.OrderDetailDTO
import com.example.frontend.ui.common.CloudinaryImage
import com.example.frontend.ui.common.QRCodeImage
import com.example.frontend.ui.navigation.Route
import kotlinx.coroutines.launch

@Composable
fun OrderScreen(
    orderViewModel: OrderViewModel = hiltViewModel(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OrderItemList(orderViewModel = orderViewModel)

        Spacer(modifier = Modifier.height(16.dp)) // Tạo khoảng cách



        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Scan QR To Checkout",
            fontSize = 20.sp
        )

        QRCodeImage(
            "00020101021238570010A000000727012700069704220113VQRQACLNP26800208QRIBFTTA530370454061000005802VN62270823Thanh toan don hang ABC6304CE4C"
        )
    }
}



@Composable
fun OrderItemList(
    orderViewModel: OrderViewModel,
) {
    val orderDetailList by orderViewModel.orderDetailList
    val productImageUrls = orderViewModel.productImageUrls

    LazyColumn() {
        items(items = orderDetailList) { orderDetail ->
            val productId = orderDetail.productId

            val imageUrl = productImageUrls[productId]

            Log.d("Order", imageUrl?.toString() ?: "Image not loaded yet for productId = $productId")

            OrderItemRow(
                orderDetailDTO = orderDetail,
                imageUrl = imageUrl ?: ""
            )
        }
    }
}





@Composable
fun OrderItemRow(
    modifier: Modifier = Modifier,
    orderDetailDTO: OrderDetailDTO,
    imageUrl: String,
) {
    Card(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //image
            CloudinaryImage(
                url = imageUrl,
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                //item name
                Text(
                    text = orderDetailDTO.productName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                //item quantity
                Text(
                    text = "Quantity: ${orderDetailDTO.quantity}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))
            //item price
            Text(
                text = "Total: ${orderDetailDTO.totalPrice.formatAsCurrency()}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}


fun Double.formatAsCurrency(): String {
    val localeVN = java.util.Locale("vi", "VN")
    val formatter = java.text.NumberFormat.getCurrencyInstance(localeVN).apply {
        maximumFractionDigits = 0
    }
    return formatter.format(this)
}