package com.example.frontend.ui.screen.home

import android.R.attr.data
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.grid.*
import com.example.frontend.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.frontend.data.model.ProductData
import com.example.frontend.data.remote.ApiResponse
import com.example.frontend.data.repository.ProductRepository
import com.example.frontend.ui.Controller.ProductController
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
fun HomeScreenContent(
    viewModel: ProductViewModel = hiltViewModel(),
    innerPadding: PaddingValues,
    onProductClick: (String) -> Unit,
){
    val state by viewModel.productState.collectAsState()

    var productList by remember { mutableStateOf<List<ProductData>>(emptyList()) }
    var searchText by remember { mutableStateOf("") }



//     Bộ lọc sản phẩm theo từ khóa tìm kiếm
//    val filteredProducts = productList.filter {
//        it.name.contains(searchText, ignoreCase = true) ||
//                it.brand.contains(searchText, ignoreCase = true)
//    }



    when(state){
        is ApiResponse.Loading -> {
            CircularProgressIndicator()
        }
        is ApiResponse.Success -> {
            val products = (state as ApiResponse.Success<List <ProductData>>).data
            Column {
                SearchBar(
                    value = searchText,
                    onValueChange = {text -> searchText = text}
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

                ProductGrid(products)
            }
        }
        is ApiResponse.Error -> {
            val error = (state as ApiResponse.Error).message
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
fun ProductGrid(products: List<ProductData>) {
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
                onClick = { /* Handle click */ }
            )
        }
    }
}
@Composable
fun ProductCard(
    product: ProductData ,
    productImage: String ? = null,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .size(width = 150.dp, height = 250.dp)
            .clickable(onClick = onClick),

        elevation =  CardDefaults.cardElevation(15.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
//                .padding(16.dp)
        ) {
            // Product image
//            Image(
//                painter = rememberAsyncImagePainter(data),
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(150.dp)
//                    .clip(RoundedCornerShape(8.dp)),
//                contentScale = ContentScale.Crop
//            )
            Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No Image", color = Color.White)
                    }


            Spacer(modifier = Modifier.height(8.dp))

            // Product name
            Card(  border = BorderStroke(2.dp,Color.Red),){
                Column(
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Product price
                    Text(
                        text = product.price.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

        }
    }
}
//
//@Composable
//@Preview( showBackground = true)
//fun ProductCardPreview(){
////    ProductCardd()
//    ProductCardd(
//        productName = "Apple iPhone 14",
//        productPrice = "$999.99",
//        productImage = "https://example.com/iphone14.jpg",
//        onClick = {}
//    )
//}



