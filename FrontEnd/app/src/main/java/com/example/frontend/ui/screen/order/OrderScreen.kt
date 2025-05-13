package com.example.frontend.ui.screen.order

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.ViewModel.OrderViewModel
import com.example.frontend.data.dto.OrderDetailDTO

@Composable
fun OrderScreen(
    orderViewModel: OrderViewModel = hiltViewModel()
) {
    val orderDetailList =  orderViewModel.orderDetailList.value
    Log.d("OrderScreen-orderDetailList", "$orderDetailList")
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(orderDetailList) { orderDetail ->
            OrderDetailItem(orderDetail = orderDetail)
        }
    }
}


@Composable
fun OrderDetailItem(orderDetail: OrderDetailDTO) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Product Name: ${orderDetail.productName}",
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Quantity: ${orderDetail.quantity}",
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Total Price: $${orderDetail.totalPrice}",
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
