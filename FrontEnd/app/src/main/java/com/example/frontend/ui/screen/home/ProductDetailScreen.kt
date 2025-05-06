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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(
    images: List<String>,
    modifier: Modifier = Modifier,
    height: Dp = 300.dp,
    autoSlideDuration: Long = 3000L
) {
    if (images.isEmpty()) return

    val pageCount = images.size
    val pagerState = rememberPagerState(pageCount = { pageCount })

    Box(modifier = modifier.height(height)) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            CloudinaryImage(
                url = images[page],
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        DotsIndicator(
            totalDots = pageCount,
            selectedIndex = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }

    // Auto slide mỗi autoSlideDuration ms
    LaunchedEffect(pagerState) {
        while (true) {
            delay(autoSlideDuration)
            val next = (pagerState.currentPage + 1) % pageCount
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
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dotSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            Box(
                modifier = Modifier
                    .size(if (index == selectedIndex) selectedDotSize else dotSize)
                    .clip(CircleShape)
                    .background(if (index == selectedIndex) activeColor else inactiveColor)
            )
        }
    }
}

@Composable
fun DetailProductScreen(
    productId: Long,
    innerPadding: PaddingValues? = null,
    clickBack: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(productId) { viewModel.getProductById(productId) }
    val state by viewModel.productDetailState

    var showNotify by remember { mutableStateOf(false) }
    LaunchedEffect(showNotify) {
        if (showNotify) {
            delay(2_000L)
            showNotify = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding ?: PaddingValues())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp)
        ) {
            // Back button
            IconButton(
                onClick = clickBack,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.errorMessage != null -> {
                    Text(
                        text = "Error: ${state.errorMessage}",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                state.product != null -> {
                    val product = state.product!!
                    val images = product.imageUrlList

                    // ← Thay Grid + Main Image bằng ImageCarousel
                    ImageCarousel(
                        images = images,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        height = 300.dp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${images.size} available classification",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(8.dp))
                        product.price?.let { orig ->
                            Text(
                                text = "đ$orig",

                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Bio",
                        style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = product.description.orEmpty(),
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Information product",
                        style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(text = "Brand: ${product.brand}")
                        Text(text = "Category: ${product.category}")
                        Text(text = "CompatibleVehicles: ${product.compatibleVehicles}")
                        Text(text = "Year Of manufacture: ${product.yearOfManufacture}")
                        Text(text = "Size: ${product.size}")
                        Text(text = "Material: ${product.material}")
                        Text(text = "Weight: ${product.weight}")

                    }
                }
            }
        }

        // Bottom bar with safe padding
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()

                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedButton(
                onClick = {
                    viewModel.addToCart()
                    showNotify = true
                },
                modifier = Modifier.size(48.dp),
                shape = CircleShape,

                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null)
            }
//            Button(
//                onClick = {  },
//                modifier = Modifier.weight(1f)
//            ) {
//                Text("Buy now")
//            }
        }

        if (showNotify) {
            Notification(
                modifier = Modifier.align(Alignment.TopCenter),
                text = "added to cart successfully!"
            )
        }
    }
}
