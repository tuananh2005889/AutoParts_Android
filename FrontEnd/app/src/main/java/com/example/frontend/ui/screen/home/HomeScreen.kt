package com.example.frontend.ui.screen.home

import androidx.compose.foundation.lazy.grid.*
import com.example.frontend.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.frontend.data.model.ProductData
import com.example.frontend.ui.common.CloudinaryImage
import com.example.frontend.ui.navigation.BottomNavBar
import com.example.frontend.ui.navigation.BottomNavHost
import com.example.frontend.ui.screen.login.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    rootNavController: NavHostController,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel
) {
    val bottomNavController = rememberNavController()
    Scaffold(
        modifier = modifier.safeDrawingPadding(),// or .padding(WindowInsets.systemBars.asPaddingValues())
        containerColor = Color.White,
        bottomBar = {
            BottomNavBar(navController = bottomNavController)
        }
    ) { innerPadding ->
        BottomNavHost(
            loginViewModel = loginViewModel,
            bottomNavController =  bottomNavController,
            rootNavController = rootNavController,
            innerPadding =  innerPadding,
            )
    }
}

@Composable
fun HomeScreenContent(
    viewModel: HomeViewModel = hiltViewModel(),
    innerPadding: PaddingValues,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier
){
    val homeUiState by viewModel.homeUiState.collectAsState()
    var productList by remember { mutableStateOf<List<ProductData>>(emptyList()) }
    var searchText by remember { mutableStateOf("") }

//     Bộ lọc sản phẩm theo từ khóa tìm kiếm
//    val filteredProducts = productList.filter {
//        it.name.contains(searchText, ignoreCase = true) ||
//                it.brand.contains(searchText, ignoreCase = true)
//    }
   when{
         homeUiState.isLoading -> {
            CircularProgressIndicator(modifier = modifier.padding(innerPadding))
        }
        homeUiState.products.isNotEmpty() -> {
            val products = homeUiState.products
            Column(
                modifier = modifier
                .padding(innerPadding)
            ) {
                SearchBar(
                    value = searchText,
                    onValueChange = {text -> searchText = text}
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                ProductGrid(
                    products,
                    onProductClick = onProductClick
                    )
            }
        }
           homeUiState.errorMessage != null  -> {
            val error = homeUiState.errorMessage
            Text(
                text = "Error: ${error}",
                color = MaterialTheme.colorScheme.error
                )
        }
    }
}

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    ){
    Row(
        modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_small))
    ){
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(stringResource(R.string.placeholder_search)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.surfaceDim,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.onSurface,
            ),
        )
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
fun ProductGrid(
    products: List<ProductData>,
    onProductClick: (String) -> Unit
    ) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource((R.dimen.padding_medium))),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = products) { product ->
            ProductCard(
                product = product,
                onClick = { onProductClick(product.productId)}
            )
        }
    }
}
@Composable
fun ProductCard(
    product: ProductData ,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .size(width = 150.dp, height = 270.dp)
            .clickable(onClick =  onClick),

        elevation =  CardDefaults.cardElevation(15.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if(product.imageUrlList.isNotEmpty()){
                val productImage = product.imageUrlList.random()
                CloudinaryImage(
                    url = productImage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }else{
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Image", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                ,
               verticalArrangement = Arrangement.SpaceBetween,
            ){
                // Product name
                Column(){
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Product price
                    Text(
                        text = "$ ${product.price}"
                        ,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Row(
                    modifier =  Modifier
                        .fillMaxWidth()
                        .padding(bottom = dimensionResource(R.dimen.padding_small))
                    ,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){

                    OutlinedButton(
                        modifier = Modifier
                            .height(40.dp)
                            .width(75.dp)
                        ,
                        onClick = {}
                    ) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Add to cart"
                        )
                    }
                    Button(
                        colors =  ButtonDefaults.buttonColors(),
                        modifier = Modifier
                            .height(40.dp)
                            .width(75.dp)
                        ,
                        onClick = {}
                    ) {
                        Text(text = "Buy")
                    }
                }
            }
        }
    }
}