package com.example.frontend.ui.screen.order

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.ViewModel.OrderViewModel
import com.example.frontend.data.dto.OrderDetailDTO
import com.example.frontend.ui.common.CloudinaryImage
import com.example.frontend.ui.common.QRCodeImage
import com.example.frontend.ui.common.formatAsCurrency
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    orderViewModel: OrderViewModel = hiltViewModel(),
) {
    // Màu sắc chủ đạo
    val primaryColor = Color(0xFFF15D43)
    val secondaryColor = Color(0xFFF5F7F6)
    val textPrimary = Color(0xFF1A2E35)
    val textSecondary = Color(0xFF6B818C)

    // State từ ViewModel
    val currentQrCode by remember { orderViewModel.currentQRCode }
    val showMessage by remember { derivedStateOf { orderViewModel.showPaymentMessage } }
    val hasPendingOrder by remember { orderViewModel.hasPendingOrder }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Order",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(secondaryColor)
                .padding(  top    = paddingValues.calculateTopPadding() - 30.dp,)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.statusBars.asPaddingValues())
            ) {


                if (hasPendingOrder) {
                    OrderContent(
                        orderViewModel = orderViewModel,
                        currentQrCode = currentQrCode,
                        primaryColor = primaryColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )
                } else {
                    EmptyOrderPlaceholder(textPrimary, textSecondary)
                }
            }

            if (showMessage) {
                PaymentSuccessDialog(
                    onDismiss = { orderViewModel.dismissPaymentMessage() },
                    primaryColor = primaryColor
                )
            }
        }
    }
}


@Composable
private fun OrderContent(
    orderViewModel: OrderViewModel,
    currentQrCode: String,
    primaryColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(orderViewModel.orderDetailList.value) { item ->
                OrderItemCard(
                    item = item,
                    imageUrl = orderViewModel.productImageUrls[item.productId] ?: "",
                    primaryColor = primaryColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp).fillMaxWidth())

        // Section QR Code
        if (currentQrCode.isNotEmpty()) {
            QRCodeSection(
                qrCode = currentQrCode,
                primaryColor = primaryColor,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .shadow(12.dp, RoundedCornerShape(24.dp))
            )
        }
    }
}

@Composable
private fun OrderItemCard(
    item: OrderDetailDTO,
    imageUrl: String,
    primaryColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CloudinaryImage(
                url = imageUrl,
                contentDescription = "Product image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.productName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Quantity: ${item.quantity}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = textSecondary
                    )
                )
            }

            Text(
                text = item.totalPrice.formatAsCurrency(),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = primaryColor,
                    fontWeight = FontWeight.Black
                )
            )
        }
    }
}

@Composable
private fun QRCodeSection(
    qrCode: String,
    primaryColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "SCAN TO PAY",
            style = MaterialTheme.typography.titleLarge.copy(
                color = primaryColor,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        QRCodeImage(
            qrText = qrCode,
//            modifier = Modifier
//                .size(240.dp)
//                .clip(RoundedCornerShape(16.dp))
        )
    }
}

@Composable
private fun EmptyOrderPlaceholder(
    textPrimary: Color,
    textSecondary: Color
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.cart),
            contentDescription = "Empty order",
            tint = textSecondary.copy(alpha = 0.4f),
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "No Active Orders",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = textPrimary,
                fontWeight = FontWeight.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Your active orders will appear here",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = textSecondary,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
private fun PaymentSuccessDialog(
    onDismiss: () -> Unit,
    primaryColor: Color
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Success",
                tint = primaryColor
            )
        },
        title = {
            Text(
                "Payment Successful!",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            Text(
                "Your payment has been processed successfully",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor
                )
            ) {
                Text("OK", fontWeight = FontWeight.Bold)
            }
        }
    )
}

