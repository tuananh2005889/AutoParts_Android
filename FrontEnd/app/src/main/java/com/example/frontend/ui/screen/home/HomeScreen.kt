package com.example.frontend.ui.screen.home

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
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

import com.example.frontend.ui.navigation.HomeNavHost
import com.example.frontend.ViewModel.LoginViewModel

import com.example.frontend.ui.theme.specialGothicFontFamiLy
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    rootNavController: NavHostController,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color(0xFF1A2E35)
        )
    }

    val bottomNavController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier
            .background(Color(0xFFF5F7F6)) // Light background
            .statusBarsPadding(),
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) } ,
//                bottomBar = { BottomNavBar(navController = bottomNavController) }
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
}

@Composable
fun HomeScreenContent(
    viewModel: HomeViewModel = hiltViewModel(),
    innerPadding: PaddingValues,
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    onShowSnackBar: (String) -> Unit,
) {
    // Color scheme
    val primaryColor = Color(0xFFF15D43)
    val secondaryColor = Color(0xFFF5F7F6) // Light background
    val accentColor = Color(0xFFFF7D33) // Vibrant orange
    val textPrimary = Color(0xFF1A2E35) // Dark teal
    val textSecondary = Color(0xFF6B818C) // Grayish teal
    val cardBackground = Color.White

    val homeUiState by viewModel.homeUiState.collectAsState()
    var searchText by remember { mutableStateOf("") }
    val filteredProducts = if (searchText.isBlank()) {
        homeUiState.products
    } else {
        homeUiState.products.filter {
            it.name.contains(searchText.trim(), ignoreCase = true)
        }
    }
    Column(
        modifier = modifier
            .padding(innerPadding)
            .background(secondaryColor)
    ) {
        // Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(primaryColor)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Column {
                Text(
                    text = "AutoParts Shop",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Search Bar
                SearchBar(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        when {
            homeUiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = primaryColor,
                        strokeWidth = 3.dp
                    )
                }
            }

            homeUiState.errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${homeUiState.errorMessage}",
                        color = Color(0xFFD32F2F),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            homeUiState.products.isNotEmpty() -> {
                val filteredProducts = if (searchText.isBlank()) {
                    homeUiState.products
                } else {
                    homeUiState.products.filter {
                        it.name.contains(searchText.trim(), ignoreCase = true)
                    }
                }


                // Hero Carousel
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    SlideCarousel(
                        images = listOf(R.drawable.hero1, R.drawable.hero2, R.drawable.hero3),
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                    )
                }


                Text(
                    text = "Featured Products",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = textPrimary,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 8.dp)
                )

                ProductGrid(
                    products = filteredProducts,
                    onProductClick = onProductClick,
                    homeViewModel = viewModel,
                    onShowSnackBar = onShowSnackBar,
                    cardBackground = cardBackground,
                    primaryColor = primaryColor,
                    accentColor = accentColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )
            }
        }
    }
}


@Composable
fun CategoryChip(category: String) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .clickable { /* Handle category selection */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF2A7F62),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun ProductGrid(
    products: List<ProductData>,
    onProductClick: (Long) -> Unit,
    homeViewModel: HomeViewModel,
    onShowSnackBar: (String) -> Unit,
    cardBackground: Color,
    primaryColor: Color,
    accentColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    val coroutineScope = rememberCoroutineScope()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
//        modifier = Modifier.padding(bottom = 80.dp)
    ) {
        items(products) { product ->
            ProductCard(
                product = product,
                onProductClick = { onProductClick(product.productId) },
                onAddToCartClick = {
                    coroutineScope.launch {
                        val cartItem = homeViewModel.addOneProductToCart(product.productId)
                        Log.d("cartItem", cartItem.toString())
                        if (cartItem != null) {
                            onShowSnackBar("Added ${cartItem.productName} to cart")
                        }
                    }
                },
                cardBackground = cardBackground,
                primaryColor = primaryColor,
                accentColor = accentColor,
                textPrimary = textPrimary,
                textSecondary = textSecondary
            )
        }
    }
}

@Composable
fun ProductCard(
    product: ProductData,
    onProductClick: () -> Unit,
    onAddToCartClick: () -> Unit,
    cardBackground: Color,
    primaryColor: Color,
    accentColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),

        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onProductClick)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEEEEEE))
            ) {
                CloudinaryImage(
                    url = product.imageUrlList.randomOrNull().orEmpty(),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Product Name
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Brand
            Text(
                text = product.brand,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = textSecondary
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Price Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                    Text(
                        text = "${product.price?.formatAsCurrency()}",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )
                    )

            }

            Spacer(modifier = Modifier.height(8.dp))

            // Add to Cart Button
            Button(
                onClick = onAddToCartClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF15D43),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 4.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Add to cart",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add to Cart")
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
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = Color(0xFF6B818C)
            )
        },
        placeholder = {
            Text(
                "Search auto parts...",
                color = Color(0xFF6B818C)
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = Color(0xFF1A2E35)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .shadow(2.dp, RoundedCornerShape(16.dp))
    )
}

fun Double.formatAsCurrency(): String {
    val formatter = java.text.NumberFormat.getCurrencyInstance().apply {
        maximumFractionDigits = 0
    }
    return formatter.format(this)
}