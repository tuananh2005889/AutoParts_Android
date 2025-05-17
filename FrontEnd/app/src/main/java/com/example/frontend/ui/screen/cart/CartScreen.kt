package com.example.frontend.ui.screen.cart

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.frontend.R
import com.example.frontend.ViewModel.CartViewModel
import com.example.frontend.data.dto.CartItemDTO
import com.example.frontend.ui.common.CloudinaryImage
import com.example.frontend.ui.common.Notification
import com.example.frontend.ui.common.SimpleDialog
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    // 1. Trong suốt status bar để Monet accent hiển thị
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = false
        )
    }

    // Theme colors
    val primaryColor = Color(0xFFF15D43)
    val secondaryColor = Color(0xFFF4F4F4)
    val textPrimary = Color(0xFF1A2E35)
    val textSecondary = Color(0xFF6B818C)

    // State từ ViewModel
    val cartItems by cartViewModel.cartItemDTOList
    val imageUrls by cartViewModel.imageUrlPerCartItemList
    val errorMsg by cartViewModel.errorMessage
    val totalPrice by cartViewModel.cartTotalPrice
    val hasPending by cartViewModel.hasPendingOrder

    var showError by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(errorMsg) {
        errorMsg?.let {
            showError = true
            delay(2000)
            showError = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MY CART", fontSize = 20.sp, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),

            )
        },
        containerColor = secondaryColor
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .background(secondaryColor)
                .padding(paddingValues)
        ) {
            Column(Modifier.fillMaxSize()) {
                Spacer(Modifier.height(16.dp))

                if (cartItems.isEmpty()) {
                    EmptyCartPlaceholder(textPrimary, textSecondary)
                } else {
                    CartItemsList(
                        items = cartItems,
                        images = imageUrls,
                        primaryColor = primaryColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        onIncrease = { cartViewModel.increaseQuantity(it) },
                        onDecrease = { cartViewModel.decreaseQuantity(it) },
                        onRemove = { cartViewModel.removeItemFromCart(it) }
                    )
                }
            }

            // Floating Checkout Bar ở đáy Box
            if (cartItems.isNotEmpty()) {
                FloatingCheckoutBar(
                    total = totalPrice,
                    primaryColor = primaryColor,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = (-16).dp),
                    onCheckout = {
                        if (hasPending) showDialog = true
                        else cartViewModel.clickOrderNow(navController)
                    }
                )
            }

            // Thông báo lỗi ở trên cùng
            if (showError) {
                Notification(
                    modifier = Modifier.align(Alignment.TopCenter),
                    text = errorMsg ?: "Unknown error"
                )
            }

            // Dialog pending order ở giữa Box
            SimpleDialog(
                showDialog = showDialog,
                onDismiss = { showDialog = false },
                title = "Pending Order",
                text = "Please complete your previous order before placing a new one."
            )
        }
    }
}

@Composable
fun FloatingCheckoutBar(
    total: Double,
    primaryColor: Color,
    modifier: Modifier = Modifier,
    onCheckout: () -> Unit
) {
    Box(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(24.dp))
            .background(color = Color.White, shape = RoundedCornerShape(24.dp))
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "TOTAL",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                )
                Text(
                    text = total.formatAsCurrency(),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = primaryColor,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }
            Button(
                onClick = onCheckout,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = Color.White
                ),
                modifier = Modifier.height(48.dp)
            ) {
                Text("CHECKOUT", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CartItemsList(
    items: List<CartItemDTO>,
    images: List<String>,
    primaryColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    onIncrease: (Long) -> Unit,
    onDecrease: (Long) -> Unit,
    onRemove: (Long) -> Unit
) {
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        itemsIndexed(items) { _, item ->
            val idx = items.indexOf(item)
            val imageUrl = images.getOrNull(idx) ?: ""
            SwipeableCartItem(
                item = item,
                imageUrl = imageUrl,
                primaryColor = primaryColor,
                textPrimary = textPrimary,
                textSecondary = textSecondary,
                onIncrease = { onIncrease(item.cartItemId) },
                onDecrease = { onDecrease(item.cartItemId) },
                onRemove = { onRemove(item.cartItemId) }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SwipeableCartItem(
    item: CartItemDTO,
    imageUrl: String,
    primaryColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    val dismissState = rememberDismissState()
    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == DismissValue.DismissedToEnd) {
            onRemove()
        }
    }

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd),
        background = {
            val bgColor by animateColorAsState(
                if (dismissState.targetValue == DismissValue.Default) Color.Transparent
                else Color(0xFFEF5350)
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(bgColor)
                    .padding(start = 20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (bgColor != Color.Transparent) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        },
        dismissContent = {
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CloudinaryImage(
                        url = imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            item.productName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            ),
                            maxLines = 2,
                            overflow =	TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            (item.price ?: 0.0).formatAsCurrency(),
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = primaryColor,
                                fontWeight = FontWeight.Black
                            )
                        )
                        Spacer(Modifier.height(12.dp))
                        QuantitySelector(
                            currentQuantity = item.quantity,
                            onIncrease = onIncrease,
                            onDecrease = onDecrease,
                            primaryColor = primaryColor,
                            textSecondary = textSecondary
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun QuantitySelector(
    currentQuantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    primaryColor: Color,
    textSecondary: Color
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = primaryColor.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, textSecondary.copy(alpha = 0.2f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(40.dp)
        ) {
            IconButton(onClick = onDecrease, modifier = Modifier.size(40.dp)) {
                Icon(Icons.Default.Remove, contentDescription = null, tint = primaryColor)
            }
            Text(
                "$currentQuantity",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            IconButton(onClick = onIncrease, modifier = Modifier.size(40.dp)) {
                Icon(Icons.Default.Add, contentDescription =	null, tint = primaryColor)
            }
        }
    }
}

@Composable
private fun EmptyCartPlaceholder(
    textPrimary: Color,
    textSecondary: Color
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment =	Alignment.CenterHorizontally,
        verticalArrangement =	Arrangement.Center
    ) {
        Icon(
            painter =	painterResource(id =	R.drawable.cart),
            contentDescription =	null,
            tint =	textSecondary.copy(alpha = 0.4f),
            modifier = Modifier.size(120.dp)
        )
        Spacer(Modifier.height(24.dp))
        Text(
            "Your Cart is Empty",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = textPrimary,
                fontWeight = FontWeight.Black
            )
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Explore our products and	add items to your cart",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = textSecondary,
                textAlign =	TextAlign.Center
            )
        )
    }
}

fun	Double.formatAsCurrency(): String {
    val formatter =	NumberFormat.getNumberInstance(Locale("vi", "VN")).apply {
        maximumFractionDigits = 0
        isGroupingUsed = true
    }
    return	"${formatter.format(this)} VNĐ"
}
