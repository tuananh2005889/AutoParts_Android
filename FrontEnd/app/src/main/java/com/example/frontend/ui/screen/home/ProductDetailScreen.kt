package com.example.frontend.ui.screen.home

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R
import com.example.frontend.ui.common.CloudinaryImage
import com.example.frontend.ui.common.Notification
import kotlinx.coroutines.delay

@Composable
fun DetailProductScreen(
    productId: Long,
    innerPadding: PaddingValues ? = null,
    productDetailViewModel: ProductDetailViewModel = hiltViewModel(),
    clickBack: () -> Unit,

){
    LaunchedEffect(productId) {
        productDetailViewModel.getProductById(productId)
    }
    val state by  productDetailViewModel.productDetailState

    var isVisible by remember {mutableStateOf(false)}

    LaunchedEffect(isVisible){
        if(isVisible){
           delay(2000L)
            isVisible = false
        }
    }



    Column(
        modifier = Modifier.fillMaxSize()
    ){
        Button(
            modifier = Modifier.size(width = 70.dp, height = 50.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onPrimary,
                backgroundColor = MaterialTheme.colorScheme.primary
            ),
            onClick = clickBack,
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
            )
        ){


            when{
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator(modifier = Modifier.size(100.dp))
                    }
                }

                state.errorMessage != null -> {
                    val error = state.errorMessage
                    Text(
                        modifier = Modifier.fillMaxSize(),
                        text = "Error: ${error}",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                state.product != null -> {
                    val product = state.product
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start =  16.dp, end = 16.dp)
                    ) {
                        val productImageUrlList: List<String> = product?.imageUrlList ?: emptyList()

                        Column(modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 100.dp)
                            .verticalScroll(rememberScrollState()) // help composable can scroll
                        ){
                                // Product Images
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(items = productImageUrlList) { url ->
                                        CloudinaryImage(
                                            url = url,
                                            modifier = Modifier
                                                .size(300.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Product Name
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ){

                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                        ),
                                        shape = RoundedCornerShape(
                                            topStart = 0.dp,
                                            topEnd = 10.dp,
                                            bottomEnd = 0.dp,
                                            bottomStart = 10.dp
                                        ),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                                    ){
                                        Text("Honda 94109-14000 Washer, Drain Plug (14MM)",
                                            color = LocalContentColor.current,
                                            modifier = Modifier.padding(8.dp),
                                            fontWeight = FontWeight(800),
                                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        )

                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                // Product Description
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                    ,
                                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        contentColor = MaterialTheme.colorScheme.onSurface,
                                    ),
                                    shape = RoundedCornerShape(
                                        topStart = 0.dp,
                                        topEnd = 10.dp,
                                        bottomEnd = 0.dp,
                                        bottomStart = 10.dp
                                    )
                                ){
                                   Column(
                                       modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                                   ){

                                       Card(
                                           modifier = Modifier.fillMaxWidth(),
                                           colors = CardDefaults.cardColors(
                                               containerColor = MaterialTheme.colorScheme.secondary,
                                               contentColor = MaterialTheme.colorScheme.onSecondary,
                                           ),
                                           shape = RoundedCornerShape(
                                               topStart = 10.dp,
                                               topEnd = 10.dp,
                                               bottomEnd = 0.dp,
                                               bottomStart = 0.dp
                                           )
                                       ){
                                           Text(
                                               modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                               text = "Description",
                                               fontWeight = FontWeight(800)
                                           )
                                       }

                                       Card(
                                           colors= CardDefaults.cardColors(
                                               containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                               contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                           ),
                                           modifier = Modifier.padding(top = 8.dp)
                                       ){
                                           Text( text = product!!.description
                                           )
                                       }
                                   }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                // About Product
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 80.dp)
                                    ,
                                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        contentColor = MaterialTheme.colorScheme.onSurface,
                                    ),
                                    shape = RoundedCornerShape(
                                        topStart = 0.dp,
                                        topEnd = 10.dp,
                                        bottomEnd = 0.dp,
                                        bottomStart = 10.dp
                                    )
                                ){
                                    Column(
                                        modifier = Modifier
                                            .padding(8.dp)
                                    ) {
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.secondary,
                                                contentColor = MaterialTheme.colorScheme.onSecondary,
                                            ),
                                            shape = RoundedCornerShape(
                                                topStart = 10.dp,
                                                topEnd = 10.dp,
                                                bottomEnd = 0.dp,
                                                bottomStart = 0.dp
                                            )
                                        ){
                                            Text(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                                text = "About Product",
                                                fontWeight = FontWeight(800)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(10.dp),
                                            modifier = Modifier.padding(bottom = 10.dp)
                                        ){
                                            Card(
                                                elevation =  CardDefaults.cardElevation(10.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                                )
                                            ){
                                                Text(
                                                    modifier = Modifier
                                                        .padding(horizontal = 10.dp, vertical = 5.dp),
                                                    text = "Brand: ${product!!.brand}")
                                            }
                                            Card(
                                                elevation =  CardDefaults.cardElevation(10.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                                )
                                            ){
                                                Text(
                                                    modifier = Modifier
                                                        .padding(horizontal = 10.dp, vertical = 5.dp),
                                                    text = "Category: ${product!!.category}")
                                            }
                                            Card(
                                                elevation =  CardDefaults.cardElevation(10.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                                )
                                            ){
                                                Text(
                                                    modifier = Modifier
                                                        .padding(horizontal = 10.dp, vertical = 5.dp),
                                                    text = "Compatible Vehicles: ${product!!.compatibleVehicles}")
                                            }
                                            Card(
                                                elevation =  CardDefaults.cardElevation(10.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                                )
                                            ){
                                                Text(
                                                    modifier = Modifier
                                                        .padding(horizontal = 10.dp, vertical = 5.dp),
                                                    text = "Year Of Manufacture: ${product!!.yearOfManufacture}")
                                            }
                                            Card(
                                                elevation =  CardDefaults.cardElevation(10.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                                )
                                            ){
                                                Text(
                                                    modifier = Modifier
                                                        .padding(horizontal = 10.dp, vertical = 5.dp),
                                                    text = "Size: ${product!!.size}")
                                            }
                                            Card(
                                                elevation =  CardDefaults.cardElevation(10.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                                )
                                            ){
                                                Text(
                                                    modifier = Modifier
                                                        .padding(horizontal = 10.dp, vertical = 5.dp),
                                                    text = "Material: ${product!!.material}")
                                            }
                                            Card(
//                                            modifier = Modifier.padding(10.dp),
                                                elevation =  CardDefaults.cardElevation(10.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                                )
                                            ){
                                                Text(
                                                    modifier = Modifier
                                                        .padding(horizontal = 10.dp, vertical = 5.dp),
                                                    text = "Weight: ${product!!.weight}")
                                            }
                                        }
                                    }
                                }
                            }

                        if(isVisible){
                            Notification(
                                modifier = Modifier.align(Alignment.TopCenter),
                                text = "Added to cart successfully!"
                            )
                        }

                        // Add to cart and +/- quantity
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 65.dp),
                        ){
                            val quantity = productDetailViewModel.quantity.value
                            val totalPrice = productDetailViewModel.totalPrice.value
                            Card(modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .height(50.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.background),
                                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                            )
                            {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                    ,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "$ ${totalPrice}",
                                        modifier = Modifier.padding(start = 40.dp),
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)

                                    ){
                                        OutlinedButton (
                                            onClick={productDetailViewModel.decreaseQuantity()},
                                            modifier = Modifier.size(width=30.dp, height=30.dp),
                                            shape = CircleShape,
                                            contentPadding = PaddingValues(9.dp)
                                        ){
                                            Icon(
                                                painter = painterResource(R.drawable.minus_sign),
                                                contentDescription = null)
                                        }
                                        Text("${quantity}")
                                        OutlinedButton (
                                            onClick={ productDetailViewModel.increaseQuantity() },
                                            modifier = Modifier.size(width=35.dp, height=35.dp),
                                            shape = CircleShape,
                                            contentPadding = PaddingValues(10.dp)
                                        ){
                                            Icon(
                                                imageVector = Icons.Default.Add, contentDescription = null)
                                        }

                                    }
                                    Button (
                                        onClick = {productDetailViewModel.addToCart()
                                                  isVisible = true },
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = MaterialTheme.colorScheme.onPrimary,
                                            backgroundColor = MaterialTheme.colorScheme.primary,
                                        ),
                                        modifier = Modifier.padding(end = 8.dp),
                                        contentPadding = PaddingValues(8.dp)
                                    ) {
                                        Text(text = "Add to cart")
                                    }

                                }
                            }
                        }


                    }
                }

            }
        }
    }


}


