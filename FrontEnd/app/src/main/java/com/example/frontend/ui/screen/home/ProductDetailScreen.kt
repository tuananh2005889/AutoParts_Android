package com.example.frontend.ui.screen.home
import androidx.compose.foundation.lazy.items
import com.example.frontend.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import  androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.ui.common.CloudinaryImage

@Composable
fun DetailProductScreen(
    productId: String,
    innerPadding: PaddingValues ? = null,
    productDetailViewModel: ProductDetailViewModel = hiltViewModel(),
    clickBack: () -> Unit,
){
    LaunchedEffect(productId) {
        productDetailViewModel.getProductById(productId)
    }
    val state by  productDetailViewModel.productDetailState

    Column(modifier = Modifier.fillMaxSize()){
        Row (
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ){
            Card(modifier = Modifier.fillMaxSize(), border = BorderStroke(1.dp, MaterialTheme.colorScheme.background)){
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart){
                    Button(
                        modifier = Modifier.size(width = 70.dp, height = 50.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            backgroundColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        onClick = clickBack,
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                }

            }
        }

        when{
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator(modifier = Modifier.size(100.dp))
                }
            }

            state.product != null -> {
                val product = state.product
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val productImageUrlList: List<String> = product?.imageUrlList ?: emptyList()
                    //Product detail
                    Column(modifier = Modifier.fillMaxSize()){
                        LazyRow(
                                contentPadding = PaddingValues(16.dp),
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

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 16.dp)
                        ){

                            Card(modifier =  Modifier.fillMaxSize(),
                                border = BorderStroke(1.dp, Color.Red),
                                shape = RoundedCornerShape(2.dp)
                            ){
//                                Text(text = product?.name ?: "Null")
                                Text("Honda 94109-14000 Washer, Drain Plug (14MM)",
                                    fontWeight = FontWeight(800),
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                    )

                            }
                        }

                    }


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter).height(120.dp).padding(bottom = 70.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center

                            ){
                                Card(modifier = Modifier
                                    .padding(horizontal = 20.dp)

                                    .height(80.dp),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.background),
                                    elevation = 5.dp
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
                                            text = "$ ${product?.price ?: 0}",
                                            modifier = Modifier.padding(start = 40.dp),
                                        )
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)

                                        ){
                                            OutlinedButton (
                                                modifier = Modifier.size(width=30.dp, height=30.dp),
                                                onClick={},
                                                shape = CircleShape,
                                                contentPadding = PaddingValues(9.dp)
                                            ){
                                                Icon(
                                                    painter = painterResource(R.drawable.minus_sign),
                                                    contentDescription = null)
                                            }
                                            Text("1")
                                            OutlinedButton (
                                                modifier = Modifier.size(width=35.dp, height=35.dp),
                                                onClick={},
                                                shape = CircleShape,
                                                contentPadding = PaddingValues(10.dp)
                                            ){
                                                Icon(
                                                    imageVector = Icons.Default.Add, contentDescription = null)
                                            }


                                        }
                                        Button (
                                            colors = ButtonDefaults.buttonColors(
                                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                                backgroundColor = MaterialTheme.colorScheme.secondaryContainer
                                            )
                                            ,
                                            onClick = {},
                                            modifier = Modifier

                                                .height(50.dp)
                                                .width(100.dp)
                                            ,
                                            contentPadding = PaddingValues(8.dp)
                                        ) {
                                            Text(text = "Add to cart")
                                        }

                                    }
                                }
                    }


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
        }
    }
    }


