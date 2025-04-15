package com.example.frontend.ui.screen.home

import android.R
import android.R.attr.onClick
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import  androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.data.model.ProductData
import com.example.frontend.data.remote.ApiResponse
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.coroutineContext

@Composable
fun DetailProductScreen(
    productId: String,
    innerPadding: PaddingValues ? = null,
    productViewModel: ProductDetailViewModel = hiltViewModel()

){
    LaunchedEffect(productId) {
        productViewModel.getProductById(productId)
    }
    val productState = productViewModel.productState.value

    when(productState) {
        is ApiResponse.Loading -> {
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(modifier = Modifier.size(100.dp))
            }
        }

        is ApiResponse.Success -> {
            val product = productState.data
            Box(
                modifier = Modifier.fillMaxSize()
            )
            {

                Card(modifier = Modifier
                    .padding(bottom = 60.dp)
                    .height(80.dp)
                    .align(Alignment.BottomCenter)
                    , border = BorderStroke(2.dp, Color.Black))
                {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal =25.dp)
                        ,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$ ${product.price}",
                            modifier = Modifier.padding(start = 40.dp),
                        )
                        Button (
                            colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                backgroundColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                            ,
                            onClick = {},
                            modifier = Modifier

                                .height(50.dp)
                                .width(130.dp)
                            ,
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            Text(text = "Add to cart")
                        }
                    }
                }


            }
        }
        is ApiResponse.Error -> {
            val error = productState.message
            Text(
                modifier = Modifier.fillMaxSize(),
                text = "Error: ${error}",
                color = MaterialTheme.colorScheme.error
            )
        }
        }
    }


