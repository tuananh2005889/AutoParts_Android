package com.example.frontend.ui.screen.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Output
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.frontend.data.dto.OrderDTO
import androidx.compose.runtime.setValue
import com.example.frontend.ui.common.OrderItemCard
import com.example.frontend.ui.common.formatAsCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AwaitingShipmentScreen(
    awaitingShipmentViewModel: AwaitingShipmentViewModel = hiltViewModel(),
    onClick: ()->Unit
){
    val orderList by remember { awaitingShipmentViewModel.orderList }

    Scaffold(
        topBar = {
            TopAppBar(

                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Text(text = "Awaiting Shipment")
                        Button(
                            onClick = onClick
                        ) {
                            Icon(Icons.Default.Output, contentDescription = null)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) {
            paddingValues ->
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding() )
        ){

            OrderList(
                orderList,
                awaitingShipmentViewModel = awaitingShipmentViewModel
            )
        }

    }


}


@Composable
fun OrderList(
    orderList: List<OrderDTO>,
    awaitingShipmentViewModel: AwaitingShipmentViewModel
) {
    LazyColumn {
        items(orderList) { orderDTO ->
            var expanded by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(8.dp))
                    .clickable { expanded = !expanded }
                    .padding(12.dp)
            ) {
                Column(){
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Order: ${orderDTO.orderCode}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Toggle Items"
                        )
                    }
                    Text(
                        text = "Create at: ${orderDTO.createTime}",
                    )
                    Text(
                        text = "Total price: ${orderDTO.totalPrice.formatAsCurrency()}"
                    )
                }

                AnimatedVisibility(visible = expanded) {
                    Column {
                        orderDTO.orderDetailDTOList.forEach { orderDetailDTO ->
                            OrderItemCard(
                                item =  orderDetailDTO,
                                modifier = Modifier.padding(vertical = 8.dp),
                                imageUrl = awaitingShipmentViewModel.productImageUrlMap[orderDetailDTO.productId].toString()
                            )
                        }
                    }
                }
            }
        }
    }
}

