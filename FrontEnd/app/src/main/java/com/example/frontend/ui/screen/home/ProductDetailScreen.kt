package com.example.frontend.ui.screen.home
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp

import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.ViewModel.ProductDetailViewModel
import com.example.frontend.ui.common.CloudinaryImage
import com.example.frontend.ui.common.Notification
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextButton
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import com.example.frontend.ViewModel.ReviewViewModel
import com.example.frontend.data.model.AddReviewRequest
import com.example.frontend.data.model.ReviewData
import kotlinx.coroutines.delay
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import com.example.frontend.ui.common.formatAsCurrency
import java.time.LocalDateTime
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailProductScreen(
    productId: Long,
    innerPadding: PaddingValues = PaddingValues(),
    clickBack: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel(),
    reviewVm: ReviewViewModel = hiltViewModel()
) {
    // Color scheme
    val primaryColor = Color(0xFF0066CC)
    val secondaryColor = Color(0xFFE3F2FD)
    val accentColor = Color(0xFFFF6D00)
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color(0xFFFAFAFA))
    ) {
        // Main scrollable content
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
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .navigationBarsPadding()
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
                            // Image carousel with shadow
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

                            // Product info section
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

                            // Product details section
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

                                    // Details grid
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(2),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.height(200.dp)
                                    ) {
                                        item {
                                            DetailItem("Brand", product.brand)
                                        }
                                        item {
                                            DetailItem("Category", product.category)
                                        }
                                        item {
                                            DetailItem("Compatibility", product.compatibleVehicles)
                                        }
                                        item {
                                            DetailItem("Year", product.yearOfManufacture.toString())
                                        }
                                        item {
                                            DetailItem("Size", product.size)
                                        }
                                        item {
                                            DetailItem("Material", product.material)
                                        }
                                        item {
                                            DetailItem("Weight", product.weight.toString())
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Reviews section header
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
                                    backgroundColor = Color(0xFFF15D43),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp),
                                elevation = ButtonDefaults.elevation(
                                    defaultElevation = 4.dp,
                                    pressedElevation = 8.dp
                                )
                            ) {
                                Text("Add Review")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Average rating display
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = primaryColor)
                    }
                }
                errorRv != null -> item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
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
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp, start = 16.dp)
        ) {
            IconButton(
                onClick = clickBack,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        // Bottom action bar with floating effect
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
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
                        .background(
                            color = secondaryColor,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = Color(0xFFF15D43),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Button(
                    onClick = { /* handle buy now */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFF15D43),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Text(
                        "Buy Now",
                        style = MaterialTheme.typography.button.copy(
                            fontWeight = FontWeight.Bold
                        )
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
                        reviewVm.post(
                            AddReviewRequest(
                                productId = productId,
                                userId = 1L,  // Adjust with actual user ID
                                rating = rating,
                                comment = comment
                            )
                        ) {
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

@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.caption.copy(
                color = Color(0xFF757575)
            )
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
                // Avatar
                val avatarUrl = review.userAvatarUrl
                if (!avatarUrl.isNullOrBlank()) {
                    CloudinaryImage(
                        url = avatarUrl,
                        contentDescription = "User avatar",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Default avatar",
                        modifier = Modifier.size(48.dp),
                        tint = primaryColor
                    )
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

                    // Rating stars
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(review.rating.coerceIn(0, 5)) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(16.dp))
                        }
                        repeat((5 - review.rating).coerceAtLeast(0)) {
                            Icon(
                                Icons.Default.StarBorder,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(16.dp))
                        }
                    }
                }

                // Date
                Text(
                    text = review.createdAt.substringBefore('T'),
                    style = MaterialTheme.typography.caption.copy(
                        color = textSecondary
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = review.comment,
                style = MaterialTheme.typography.body1.copy(
                    color = textPrimary,
                    lineHeight = 20.sp
                )
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
            Text(
                "Write a Review",
                style = MaterialTheme.typography.h6.copy(
                    color = primaryColor,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            Column {
                Text(
                    "How would you rate this product?",
                    style = MaterialTheme.typography.subtitle1.copy(
                        color = Color(0xFF424242)
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = if (index < rating) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { rating = index + 1 }
                                .padding(4.dp)
                        )
                    }
                }

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onDismiss,

                ) {
                    Text("CANCEL")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (rating > 0 && comment.isNotBlank()) {
                            onSubmit(rating, comment.trim())
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = primaryColor,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = rating > 0 && comment.isNotBlank()
                ) {
                    Text("SUBMIT")
                }
            }
        },
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.White
    )
}

@Composable
fun Notification(
    modifier: Modifier = Modifier,
    text: String,
    backgroundColor: Color
) {
    Box(
        modifier = modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(
    images: List<String>,
    modifier: Modifier = Modifier,
    height: Dp = 300.dp,
    autoSlideDuration: Long = 3000L
) {
    if (images.isEmpty()) return
    val pagerState = rememberPagerState(pageCount = { images.size })
    Box(modifier = modifier.height(height)) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            CloudinaryImage(
                url = images[page],
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        DotsIndicator(
            totalDots = images.size,
            selectedIndex = pagerState.currentPage,
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
        )
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
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    dotSize: Dp = 8.dp,
    selectedDotSize: Dp = 12.dp,
    dotSpacing: Dp = 6.dp,
    activeColor: Color = Color.White,
    inactiveColor: Color = Color.LightGray
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(dotSpacing),
        verticalAlignment = Alignment.CenterVertically) {
        repeat(totalDots) { index ->
            Box(
                modifier = Modifier.size(if (index == selectedIndex) selectedDotSize else dotSize)
                    .clip(CircleShape)
                    .background(if (index == selectedIndex) activeColor else inactiveColor)
            )
        }
    }
}
@Composable
fun ReviewItem(review: ReviewData) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
        // 1. Format ngày tháng
        val date = review.createdAt.substringBefore('T').takeIf { it.isNotBlank() } ?: review.createdAt

        // 2. Avatar: chỉ gọi CloudinaryImage khi URL không null/blank
        val avatarUrl = review.userAvatarUrl
        if (!avatarUrl.isNullOrBlank()) {
            CloudinaryImage(
                url = avatarUrl,
                contentDescription = "User avatar",
                modifier = Modifier.size(40.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Default avatar",
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = review.userName, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.width(8.dp))
                // hiển thị sao
                repeat(review.rating.coerceIn(0,5)) { Icon(Icons.Default.Star, contentDescription = null) }
                repeat((5 - review.rating).coerceAtLeast(0)) { Icon(Icons.Default.StarBorder, contentDescription = null) }
            }
            Spacer(Modifier.height(4.dp))
            Text(text = review.comment)
            Spacer(Modifier.height(4.dp))
            Text(text = date, style = MaterialTheme.typography.caption)
        }
    }
}

@Composable
fun AddReviewDialog(
    onSubmit: (rating: Int, comment: String) -> Unit,
    onDismiss: () -> Unit
) {
    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Write a review") },
        text = {
            Column {
                Row {
                    repeat(5) { index ->
                        Icon(
                            imageVector = if (index < rating) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp).clickable { rating = index + 1 }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Review") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (rating > 0 && comment.isNotBlank()) {
                    onSubmit(rating, comment.trim())
                }
            }) { Text("Send") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

