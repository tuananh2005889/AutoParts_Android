package com.example.frontend.ui.common

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun QRCodeImage(qrText: String) {
    val bitmap = remember(qrText) { generateQRCode(qrText) }
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "QR Code",
        modifier = Modifier.size(200.dp)
    )
}
//fun generateQRCode(text: String): Bitmap {
//    val size = 512 // Kích thước ảnh (px)
//    val bits = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
//    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
//    for (x in 0 until size) {
//        for (y in 0 until size) {
//            bitmap.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
//        }
//    }
//    return bitmap
//}
fun generateQRCode(text: String): Bitmap {
    return try {
        val size = 512
        val bits = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        bitmap
    } catch (e: Exception) {
        Log.e("QRCode", "Error generating QR Code", e)
        // Trả về một bitmap trắng để tránh lỗi
        Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
    }
}