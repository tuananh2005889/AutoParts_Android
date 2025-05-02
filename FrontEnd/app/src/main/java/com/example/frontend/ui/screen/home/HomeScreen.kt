package com.example.frontend.ui.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.frontend.R
import com.example.frontend.ViewModel.HomeViewModel
import com.example.frontend.data.model.ProductData
import com.example.frontend.ui.common.CloudinaryImage
import com.example.frontend.ui.navigation.BottomNavBar
import com.example.frontend.ui.navigation.HomeNavHost
import com.example.frontend.ViewModel.LoginViewModel
import com.example.frontend.ui.theme.specialGothicFontFamiLy
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
                    .background(MaterialTheme.colorScheme.background)

            ) {
                Text(
                    text = "Auto Parts Shop",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = SoftWhite,
                        fontSize = 24.sp
                    ),
                    fontFamily = specialGothicFontFamiLy,
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
                            onShowSnackBar("Added 1 ${cartItem.productName} to cart")
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
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .width(200.dp)
            .wrapContentHeight()
            .clickable(onClick = onProductClick)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // --- IMAGE ---
            CloudinaryImage(
                url = product.imageUrlList.randomOrNull().orEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(8.dp))

            // --- NAME ---
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // --- BRAND ---
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

            // --- DESCRIPTION (~50 words) ---
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(8.dp))

            // --- PRICE, QUANTITY & ADD TO CART ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = product.price?.formatAsCurrency() ?: "",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = "Qty: ${product.quantity}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                IconButton(
                    onClick = onAddToCartClick,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFF15D43))
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Add to cart",
                        modifier = Modifier.size(18.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

// Ví dụ về hàm extension để format giá
fun Double.formatAsCurrency(): String {
    val formatter = java.text.NumberFormat.getCurrencyInstance().apply {
        maximumFractionDigits = 0
    }
    return formatter.format(this)
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
