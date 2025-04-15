package com.example.frontend.ui.common

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter

@Composable
fun CloudinaryImage(
    url: String,
    contentDescription: String ? = "This is the image :))",
    contentScale: ContentScale = ContentScale.Crop,
    modifier: Modifier = Modifier
    ) {
    Image(
        painter = rememberAsyncImagePainter(url),
        modifier = modifier,
        contentDescription = contentDescription,
        contentScale =  contentScale
    )
}