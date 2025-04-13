package com.example.frontend.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.frontend.Controller.OrderController
import com.example.frontend.Controller.ProductController
import com.example.frontend.ViewModel.UserViewModel
import com.example.frontend.data.model.ProductData
import com.example.frontend.ui.navigation.BottomNavBar
import com.example.frontend.ui.navigation.BottomNavHost


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    rootNavController: NavHostController,
    modifier: Modifier = Modifier
) {

    val bottomNavController = rememberNavController()
    Scaffold(
        modifier = modifier.padding(WindowInsets.systemBars.asPaddingValues()), // or modifier.safeDrawingPadding()
        containerColor = Color.White,
        bottomBar = {
            BottomNavBar(navController = bottomNavController)
        }
    ) { innerPadding ->
        BottomNavHost(
            bottomNavController =  bottomNavController,
            rootNavController = rootNavController,
            innerPadding =  innerPadding,
            )
    }
}

@Composable
fun HomeScreenContent(innerPadding: PaddingValues){
    var productList by remember { mutableStateOf<List<ProductData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf("") }
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        ProductController.getAllProducts { success, products, msg ->
            if (success && products != null) {
                productList = products
            } else {
                errorMsg = msg
            }
            isLoading = false
        }
    }

    // Bộ lọc sản phẩm theo từ khóa tìm kiếm
    val filteredProducts = productList.filter {
        it.name.contains(searchText, ignoreCase = true) ||
                it.brand.contains(searchText, ignoreCase = true)
    }

                Row{
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            placeholder = { Text("Search...", color = Color.Black) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                        )
                        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Categories",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            CategoryChip("Abc")
            CategoryChip("def")
            CategoryChip("gdads")
            CategoryChip("grtdá")
        }
        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFFF7043))
                }
            }
            errorMsg.isNotEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = errorMsg, color = Color.Red)
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredProducts) { product ->
                        ProductCard(product)
                    }
                }
            }
        }
    }
}


@Composable
fun CategoryChip(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFF7043))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = Color.White, fontSize = 14.sp)
    }
}
@Composable
fun ProductCard(product: ProductData) {
    val userViewModel: UserViewModel = viewModel()
    val currentUser by userViewModel.currentUser.collectAsState()
    var errorMessage by remember { mutableStateOf("") }
    val username by remember { mutableStateOf("") }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                if (product.images.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(product.images.first()),
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No Image", color = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 1
            )
            Text(
                text = product.brand,
                fontSize = 12.sp,
                color = Color.DarkGray,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$${product.price}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E88E5)
            )
            Text(
                text = "Quantity: ${product.quantity}",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(
                    onClick = {

                        OrderController.addToCart(
                            userName = currentUser!!.userName,
                            productId = product.productId,
                            quantity = 1
                        ) { success, message ->
                            errorMessage = message
                        }

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Add to Cart", fontSize = 12.sp, color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                // Nút "Buy Now"
                Button(
                    onClick = {

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF15D43)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Buy Now", fontSize = 12.sp, color = Color.White)
                }
            }
        }
    }
}


