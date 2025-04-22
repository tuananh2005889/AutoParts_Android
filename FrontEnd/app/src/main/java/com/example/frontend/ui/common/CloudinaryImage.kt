package com.example.frontend.ui.common

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun CloudinaryImage(
    url: String,
    contentDescription: String? = "This is the image :))",
    contentScale: ContentScale = ContentScale.Crop,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val imageRequest = ImageRequest.Builder(context)
        .data(url)
        .crossfade(true)
        .listener(
            onSuccess = { request, metadata ->
                Log.d("CloudinaryImage", "✅ Loaded: $url")
            },
            onError = { request, errorResult ->
                Log.e("CloudinaryImage", "❌ Error loading $url", errorResult.throwable)
            }
        )
        .build()


    val painter = rememberAsyncImagePainter(model = imageRequest)

    Image(
        painter = painter,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
    )
}

