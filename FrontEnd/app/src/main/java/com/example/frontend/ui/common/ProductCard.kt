package com.example.frontend.ui.common
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.frontend.data.model.ProductData
import com.example.frontend.ui.common.CloudinaryImage

@Composable
fun ProductCard(
    product: ProductData,
    onProductClick: () -> Unit,
    onAddToCartClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    cardBackground: Color = MaterialTheme.colors.surface,
    primaryColor: Color = Color(0xFFF15D43),
    accentColor: Color = Color(0xFF2A7F62),
    textPrimary: Color = Color.Black,
    textSecondary: Color = Color.Gray
) {
    Card(
        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(containerColor = cardBackground),
//        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onProductClick)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEEEEEE))
            ) {
                CloudinaryImage(
                    url = product.imageUrlList.randomOrNull().orEmpty(),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Product Name
            Text(
                text = product.name,
//                style = MaterialTheme.typography.titleMedium.copy(
//                    fontWeight = FontWeight.Bold,
//                    color = textPrimary
//                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Brand
            Text(
                text = product.brand,
//                style = MaterialTheme.typography.bodySmall.copy(
//                    color = textSecondary
//                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Price Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${product.price?.formatAsCurrency()}",
//                    style = MaterialTheme.typography.titleSmall.copy(
//                        fontWeight = FontWeight.Bold,
//                        color = primaryColor
//                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Add to Cart Button
            Button(
                onClick = onAddToCartClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFFF15D43),
//                    contentColor = Color.White
//                ),
//                elevation = ButtonDefaults.buttonElevation(
//                    defaultElevation = 2.dp,
//                    pressedElevation = 4.dp
//                )
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Add to cart",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add to Cart")
            }
        }
    }
}
