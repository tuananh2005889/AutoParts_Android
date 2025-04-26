package com.example.frontend.ui.screen.home

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.frontend.R
import com.example.frontend.data.model.ProductData
import com.example.frontend.ui.common.CloudinaryImage
import com.example.frontend.ui.navigation.BottomNavBar
import com.example.frontend.ui.navigation.HomeNavHost
import com.example.frontend.ui.screen.login.LoginViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

private val DarkBackground = Color(0xFF30393E)
private val SoftWhite = Color(0xFFFAEFEB)
private val Accent = Color(0xFFF8BD97)
private val SoftGray = Color(0xFF988F88)
private val Cream = Color(0xFFF8E5D7)
private val ElegantBrown = Color(0xFFD5C1B6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    rootNavController: NavHostController,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel
) {
    val bottomNavController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.safeDrawingPadding().background(SoftWhite),
        bottomBar = { BottomNavBar(navController = bottomNavController) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        HomeNavHost(
            loginViewModel = loginViewModel,
            bottomNavController = bottomNavController,
            rootNavController = rootNavController,
            innerPadding = innerPadding,
            onShowSnackBar = { message ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }
        )
    }
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = DarkBackground
        )
    }
}

@Composable
fun HomeScreenContent(
    viewModel: HomeViewModel = hiltViewModel(),
    innerPadding: PaddingValues,
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    onShowSnackBar: (String) -> Unit,
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    var searchText by remember { mutableStateOf("") }

    when {
        homeUiState.isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Accent)
            }
        }

        homeUiState.products.isNotEmpty() -> {
            val products = homeUiState.products
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .background(DarkBackground)

            ) {
                Text(
                    text = "Auto Parts Shop",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = SoftWhite,
                        fontSize = 24.sp
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )

                SearchBar(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(16.dp))

                SlideCarousel(
                    images = listOf(R.drawable.hero1, R.drawable.hero2, R.drawable.hero3),
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )

                Spacer(Modifier.height(16.dp))

                ProductGrid(
                    products,
                    onProductClick,
                    viewModel,
                    onShowSnackBar
                )
            }
        }

        homeUiState.errorMessage != null -> {
            Text(
                text = "Error: ${homeUiState.errorMessage}",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun ProductGrid(
    products: List<ProductData>,
    onProductClick: (Long) -> Unit,
    homeViewModel: HomeViewModel,
    onShowSnackBar: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(products) { product ->
            ProductCard(
                product = product,
                onProductClick = { onProductClick(product.productId) },
                onAddToCartClick = {
                    coroutineScope.launch {
                        val cartItem = homeViewModel.addOneProductToCart(product.productId)
                        if (cartItem != null) {
                            onShowSnackBar("Added ${cartItem.quantity} ${cartItem.productName}")
                        } else {
                            onShowSnackBar("Failed to add product to cart.")
                        }
                    }
                }
            )
        }
    }
}
@Composable
fun ProductCard(
    product: ProductData,
    onProductClick: () -> Unit,
    onAddToCartClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SoftWhite),
        elevation = CardDefaults.cardElevation(12.dp),
        modifier = Modifier
            .width(180.dp)
            .wrapContentHeight()
            .clickable(onClick = onProductClick)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            if (product.imageUrlList.isNotEmpty()) {
                CloudinaryImage(
                    url = product.imageUrlList.random(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(SoftGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Image", color = Color.White)
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                color = DarkBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (product.brand.isNotBlank()) {
                Text(
                    text = product.brand,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(6.dp))

            if (product.description.isNotBlank()) {
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(10.dp))

            product.price?.let {
                Text(
                    text = "$${"%.2f".format(it)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = ElegantBrown
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onAddToCartClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFF15D43)),
                    border = BorderStroke(1.dp, Color(0xFFF15D43)),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null)
                }

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF15D43)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Buy", color = Color.White)
                }
            }
        }
    }
}


@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null, tint = SoftGray)
        },
        placeholder = { Text("Search products...", color = SoftGray) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Accent,
            unfocusedBorderColor = SoftGray,
            cursorColor = DarkBackground
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    )
}
