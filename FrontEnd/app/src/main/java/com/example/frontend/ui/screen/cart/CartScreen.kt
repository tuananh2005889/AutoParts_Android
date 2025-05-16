package com.example.frontend.ui.screen.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.frontend.R
import com.example.frontend.ViewModel.CartViewModel
import com.example.frontend.data.dto.CartItemDTO
import com.example.frontend.ui.common.CloudinaryImage
import com.example.frontend.ui.common.Notification
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import com.example.frontend.ui.common.SimpleDialog
import com.example.frontend.ui.screen.home.formatAsCurrency

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel(),
    navController: NavHostController
) {
    // Define consistent color scheme
    val primaryColor = Color(0xFFF15D43)
    val secondaryColor = Color(0xFFF5F7F6)
    val textPrimary = Color(0xFF1A2E35)
    val textSecondary = Color(0xFF6B818C)
    val cardBackground = Color.White

    val cartItemList by cartViewModel.cartItemDTOList
    val imageUrls by cartViewModel.imageUrlPerCartItemList
    val errorMessage by cartViewModel.errorMessage
    var visible by remember { mutableStateOf(false) }
    val cartTotalPrice by cartViewModel.cartTotalPrice
    val cartId by cartViewModel._cartId.collectAsState()
    val hasPendingOrder by cartViewModel.hasPendingOrder
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            visible = true
            delay(2000L)
            visible = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(secondaryColor)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(primaryColor)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Your Shopping Cart",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                )
            }

            if (cartItemList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.cart),
                        contentDescription = "Empty cart",
                        modifier = Modifier.size(120.dp),
                        tint = textSecondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your cart is empty",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = textPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Browse our products to add items",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = textSecondary
                        )
                    )
                }
            } else {
                CartItemsList(
                    imageUrlList = imageUrls,
                    cartViewModel = cartViewModel,
                    cartItemDTOList = cartItemList,
                    cardBackground = cardBackground,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )
            }
        }

        if (cartItemList.isNotEmpty()) {
            TotalPrice(
                modifier = Modifier.align(Alignment.BottomCenter),
                totalPrice = cartTotalPrice,
                clickOrderNow = {
                    if (hasPendingOrder) {
                        showDialog = true
                    } else {
                        cartViewModel.clickOrderNow(navController)
                    }
                },
                primaryColor = primaryColor
            )
        }

        if (visible) {
            Notification(
                modifier = Modifier.align(Alignment.TopCenter),
                text = errorMessage ?: "Unknown error"
            )
        }

        SimpleDialog(
            showDialog,
            onDismiss = { showDialog = false },
            title = "Order Alert",
            text = "Please pay for the previous order"
        )
    }
}

@Composable
fun TotalPrice(
    modifier: Modifier = Modifier,
    totalPrice: Double,
    clickOrderNow: () -> Unit,
    primaryColor: Color
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Total Price",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF6B818C)
                    )
                )
                Text(
                    text = totalPrice.formatAsCurrency(),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                )
            }
            Button(
                onClick = clickOrderNow,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 4.dp
                )
            ) {
                Text(
                    text = "Checkout",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
fun CartItemsList(
    cartViewModel: CartViewModel,
    cartItemDTOList: List<CartItemDTO>,
    imageUrlList: List<String>,
    cardBackground: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp) // Space for total price
    ) {
        itemsIndexed(items = cartItemDTOList) { index, cartItemDTO ->
            val imageUrl = imageUrlList.getOrNull(index) ?: "null"
            CartItemRow(
                cartItemDTO = cartItemDTO,
                imageUrl = imageUrl,
                onClickDecrease = { cartViewModel.decreaseQuantity(cartItemDTO.cartItemId) },
                onClickIncrease = { cartViewModel.increaseQuantity(cartItemDTO.cartItemId) },
                onClickRemove = { cartViewModel.removeItemFromCart(cartItemDTO.cartItemId) },
                cardBackground = cardBackground,
                textPrimary = textPrimary,
                textSecondary = textSecondary
            )
        }
    }
}

@Composable
fun CartItemRow(
    modifier: Modifier = Modifier,
    cartItemDTO: CartItemDTO,
    imageUrl: String,
    onClickDecrease: () -> Unit,
    onClickIncrease: () -> Unit,
    onClickRemove: () -> Unit,
    cardBackground: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Remove Item") },
            text = { Text("Are you sure you want to remove this item from your cart?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onClickRemove()
                    }
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image
            CloudinaryImage(
                url = imageUrl,
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Product Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartItemDTO.productName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = (cartItemDTO.price ?: 0.0).formatAsCurrency(),
                    style = MaterialTheme.typography.bodyMedium.copy(
//                        color = primaryColor,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            // Quantity Controls
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.remove),
                        contentDescription = "Remove item",
                        tint = textSecondary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Quantity Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onClickDecrease,
                        modifier = Modifier.size(28.dp),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = textPrimary
                        ),
                        border = BorderStroke(1.dp, textSecondary)
                    ) {
                        Text("-", fontSize = 14.sp)
                    }

                    Text(
                        text = cartItemDTO.quantity.toString(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )

                    OutlinedButton(
                        onClick = onClickIncrease,
                        modifier = Modifier.size(28.dp),
                        shape = CircleShape,


                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = textPrimary
                        ),
                        border = BorderStroke(1.dp, textSecondary)
                    ) {
                        Text("+", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}