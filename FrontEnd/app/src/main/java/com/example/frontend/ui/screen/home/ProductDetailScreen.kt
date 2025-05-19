package com.example.frontend.ui.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.ViewModel.ProductDetailViewModel
import com.example.frontend.ViewModel.ReviewViewModel
import com.example.frontend.data.model.AddReviewRequest
import com.example.frontend.data.model.ReviewData
import com.example.frontend.ui.common.CloudinaryImage
import com.example.frontend.ui.common.formatAsCurrency
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailProductScreen(
    productId: Long,
    clickBack: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel(),
    reviewVm: ReviewViewModel = hiltViewModel()
) {
    // Color scheme
    val primaryColor = Color(0xFFF15D43)
    val secondaryColor = Color(0xFFE3F2FD)
    val accentColor = Color(0xFFF15D43)
    val textPrimary = Color(0xFF212121)
    val textSecondary = Color(0xFF757575)
    val surfaceColor = Color(0xFFFFFFFF)
    val dividerColor = Color(0xFFEEEEEE)

    // Load data
    LaunchedEffect(productId) {
        viewModel.getProductById(productId)
        reviewVm.load(productId)
    }

    val state by viewModel.productDetailState
    val reviews by reviewVm.reviews.collectAsState()
    val loadingRv by reviewVm.loading.collectAsState()
    val errorRv by reviewVm.error.collectAsState()

    var showNotify by remember { mutableStateOf(false) }
    var showReviewDialog by remember { mutableStateOf(false) }

    LaunchedEffect(showNotify) {
        if (showNotify) {
            delay(2000)
            showNotify = false
        }
    }

    Scaffold(
        topBar = {
                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    title = {
                        Text(
                            "Product Details",
                            color = Color.White,
                            modifier = Modifier
                                .padding(top = 20.dp)
                        )
                    },

                    navigationIcon = {
                        IconButton(onClick = clickBack,     modifier = Modifier
                            .padding(top = 20.dp)) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    backgroundColor = primaryColor,
                    elevation = 4.dp
                )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFAFAFA))
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Header section
                item {
                    when {
                        state.isLoading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = primaryColor)
                            }
                        }
                        state.errorMessage != null -> {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                elevation = 16.dp,
                                shape = RoundedCornerShape(16.dp),
                                backgroundColor = surfaceColor
                            ) {
                                Text(
                                    text = "Error: ${state.errorMessage}",
                                    color = Color(0xFFC62828),
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        state.product != null -> {
                            val product = state.product!!
                            Column {
                                // Image carousel
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    ImageCarousel(
                                        images = product.imageUrlList,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(300.dp)
                                    )
                                }

                                // Product info
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp),
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(12.dp),
                                    backgroundColor = surfaceColor
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = product.name,
                                            style = MaterialTheme.typography.h5.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = textPrimary
                                            )
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = "${product.price?.formatAsCurrency()}",
                                            style = MaterialTheme.typography.h6.copy(
                                                color = accentColor,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Text(
                                            text = product.description,
                                            style = MaterialTheme.typography.body1.copy(
                                                color = textSecondary,
                                                lineHeight = 20.sp
                                            )
                                        )
                                    }
                                }

                                // Product details grid
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(12.dp),
                                    backgroundColor = surfaceColor
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(
                                            text = "Product Details",
                                            style = MaterialTheme.typography.h6.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                color = textPrimary
                                            ),
                                            modifier = Modifier.padding(bottom = 12.dp)
                                        )

                                        LazyVerticalGrid(
                                            columns = GridCells.Fixed(2),
                                            verticalArrangement = Arrangement.spacedBy(8.dp),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.height(200.dp)
                                        ) {
                                            item { DetailItem("Brand", product.brand) }
                                            item { DetailItem("Category", product.category) }
                                            item { DetailItem("Compatibility", product.compatibleVehicles) }
                                            item { DetailItem("Year", product.yearOfManufacture.toString()) }
                                            item { DetailItem("Size", product.size) }
                                            item { DetailItem("Material", product.material) }
                                            item { DetailItem("Weight", product.weight.toString()) }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Reviews header
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = 4.dp,
                        shape = RoundedCornerShape(12.dp),
                        backgroundColor = surfaceColor
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Customer Reviews",
                                    style = MaterialTheme.typography.h6.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = textPrimary
                                    )
                                )

                                Button(
                                    onClick = { showReviewDialog = true },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = accentColor,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    elevation = ButtonDefaults.elevation(4.dp)
                                ) {
                                    Text("Add Review")
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            if (reviews.isNotEmpty()) {
                                val averageRating = reviews.map { it.rating }.average()
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "%.1f".format(averageRating),
                                        style = MaterialTheme.typography.h5.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = textPrimary
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = accentColor,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${reviews.size} reviews",
                                        style = MaterialTheme.typography.body2.copy(
                                            color = textSecondary
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // Reviews list
                when {
                    loadingRv -> item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = primaryColor)
                        }
                    }
                    errorRv != null -> item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            elevation = 4.dp,
                            backgroundColor = Color(0xFFFFEBEE)
                        ) {
                            Text(
                                text = "Error loading reviews: $errorRv",
                                color = Color(0xFFC62828),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                    reviews.isEmpty() -> item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            elevation = 4.dp,
                            backgroundColor = secondaryColor
                        ) {
                            Text(
                                text = "No reviews yet. Be the first to review!",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.body1.copy(
                                    color = textSecondary
                                )
                            )
                        }
                    }
                    else -> items(reviews) { review ->
                        ReviewItem(review, primaryColor, accentColor, textPrimary, textSecondary)
                        Divider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = dividerColor,
                            thickness = 1.dp
                        )
                    }
                }
            }

            // Bottom action bar
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                backgroundColor = surfaceColor
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            viewModel.addToCart()
                            showNotify = true
                        },
                        modifier = Modifier
                            .size(56.dp)
                            .background(secondaryColor, CircleShape)
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Button(
                        onClick = { /* handle buy now */ },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = accentColor,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.elevation(4.dp)
                    ) {
                        Text(
                            "Buy Now",
                            style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            // Notification overlay
            if (showNotify) {
                Notification(
                    modifier = Modifier.align(Alignment.TopCenter),
                    text = "Added to cart successfully!",
                    backgroundColor = primaryColor
                )
            }

            // Review dialog
            if (showReviewDialog) {
                AddReviewDialog(
                    onSubmit = { rating, comment ->
                        if (rating > 0 && comment.isNotBlank()) {
                            reviewVm.post(AddReviewRequest(productId, 1L, rating, comment)) {
                                showReviewDialog = false
                            }
                        }
                    },
                    onDismiss = { showReviewDialog = false },
                    primaryColor = primaryColor,
                    accentColor = accentColor
                )
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.caption.copy(color = Color(0xFF757575))
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF212121)
            )
        )
    }
}

@Composable
fun ReviewItem(
    review: ReviewData,
    primaryColor: Color,
    accentColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = 2.dp,
        shape = RoundedCornerShape(12.dp),
        backgroundColor = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val avatarUrl = review.userAvatarUrl
                if (!avatarUrl.isNullOrBlank()) {
                    CloudinaryImage(
                        url = avatarUrl,
                        contentDescription = "User avatar",
                        modifier = Modifier.size(48.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Default avatar", modifier = Modifier.size(48.dp), tint = primaryColor)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = review.userName,
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = textPrimary
                        )
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(review.rating.coerceIn(0, 5)) {
                            Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                        repeat((5 - review.rating).coerceAtLeast(0)) {
                            Icon(Icons.Default.StarBorder, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Text(
                    text = review.createdAt.substringBefore('T'),
                    style = MaterialTheme.typography.caption.copy(color = textSecondary)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = review.comment,
                style = MaterialTheme.typography.body1.copy(color = textPrimary, lineHeight = 20.sp)
            )
        }
    }
}

@Composable
fun AddReviewDialog(
    onSubmit: (rating: Int, comment: String) -> Unit,
    onDismiss: () -> Unit,
    primaryColor: Color,
    accentColor: Color
) {
    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Write a Review", style = MaterialTheme.typography.h6.copy(color = primaryColor, fontWeight = FontWeight.Bold))
        },
        text = {
            Column {
                Text("How would you rate this product?", modifier = Modifier.padding(bottom = 8.dp))
                Row { repeat(5) { index ->
                    Icon(
                        imageVector = if (index < rating) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp).clickable { rating = index + 1 }.padding(4.dp),
                        tint = accentColor
                    )
                }}
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Your review") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 4
                )
            }
        },
        buttons = {
            Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onDismiss) { Text("CANCEL") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { if (rating > 0 && comment.isNotBlank()) onSubmit(rating, comment.trim()) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor, contentColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("SUBMIT") }
            }
        },
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.White
    )
}

@Composable
fun Notification(modifier: Modifier = Modifier, text: String, backgroundColor: Color) {
    Box(modifier = modifier.clip(RoundedCornerShape(8.dp)).background(backgroundColor).padding(horizontal = 24.dp, vertical = 12.dp)) {
        Text(text = text, color = Color.White, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ImageCarousel(images: List<String>, modifier: Modifier = Modifier, height: Dp = 300.dp, autoSlideDuration: Long = 3000L) {
    if (images.isEmpty()) return
    val pagerState = rememberPagerState(pageCount = { images.size })
    Box(modifier = modifier.height(height)) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            CloudinaryImage(url = images[page], contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        }
        DotsIndicator(totalDots = images.size, selectedIndex = pagerState.currentPage, modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp))
    }
    LaunchedEffect(pagerState) {
        while (true) {
            delay(autoSlideDuration)
            val next = (pagerState.currentPage + 1) % images.size
            pagerState.animateScrollToPage(next)
        }
    }
}

@Composable
fun DotsIndicator(totalDots: Int, selectedIndex: Int, modifier: Modifier = Modifier, dotSize: Dp = 8.dp, selectedDotSize: Dp = 12.dp, dotSpacing: Dp = 6.dp, activeColor: Color = Color.White, inactiveColor: Color = Color.LightGray) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(dotSpacing), verticalAlignment = Alignment.CenterVertically) {
        repeat(totalDots) { index ->
            Box(modifier = Modifier.size(if (index == selectedIndex) selectedDotSize else dotSize).clip(CircleShape).background(if (index == selectedIndex) activeColor else inactiveColor))
        }
    }
}
