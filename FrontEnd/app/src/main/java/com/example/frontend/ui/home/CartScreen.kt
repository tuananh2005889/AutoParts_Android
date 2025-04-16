package com.example.frontend.ui.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.frontend.Controller.OrderController
import com.example.frontend.ViewModel.UserViewModel
import com.example.frontend.data.model.CartItemData
import com.example.frontend.ui.compon.HomeChange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavHostController, userViewModel: UserViewModel = viewModel()) {
    // Lấy thông tin user đã đăng nhập từ ViewModel
    val currentUser by userViewModel.currentUser.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf("") }
    var cartItems by remember { mutableStateOf<List<CartItemData>>(emptyList()) }

    // Khi currentUser có giá trị, gọi API getCart
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            OrderController.getCart(currentUser!!.userName) { success, items, msg ->
                if (success && items != null) {
                    cartItems = items
                } else {
                    errorMsg = msg
                }
                isLoading = false
            }
        } else {
            errorMsg = "Please log in first."
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Cart", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF7043)
                )
            )
        },
        bottomBar = {
            HomeChange(navController = navController)
        },
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFFCEAE5))
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFFFF7043)
                    )
                }
                errorMsg.isNotEmpty() -> {
                    Text(
                        text = errorMsg,
                        color = Color.Red,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(cartItems) { item ->
                            CartItemCard(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(item: CartItemData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hiển thị ảnh của sản phẩm nếu có
            if (!item.imageUrl.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(item.imageUrl),
                    contentDescription = item.productName,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Image", color = Color.White, fontSize = 10.sp)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = item.productName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "Quantity: ${item.quantity}", fontSize = 14.sp)
                Text(text = "Price: $${item.price}", fontSize = 14.sp, color = Color(0xFF1E88E5))
            }
        }
    }
}
