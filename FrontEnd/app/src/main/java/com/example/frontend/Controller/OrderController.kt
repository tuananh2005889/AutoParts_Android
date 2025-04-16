package com.example.frontend.Controller

import android.os.Handler
import android.os.Looper
import com.example.frontend.data.model.CartItemData
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class OrderController {
    companion object {
        fun addToCart(
            userName: String,
            productId: String,
            quantity: Int,
            onResult: (Boolean, String) -> Unit
        ) {
            val url = "http://10.0.2.2:8080/cart/add"
            val client = OkHttpClient()
            val payload = mapOf(
                "userName" to userName,
                "productId" to productId,
                "quantity" to quantity
            )
            val json = Gson().toJson(payload)
            val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Handler(Looper.getMainLooper()).post {
                        onResult(false, "Failed to connect to server")
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    Handler(Looper.getMainLooper()).post {
                        if (response.isSuccessful) {
                            onResult(true, "Product added to cart successfully")
                        } else {
                            onResult(false, "Failed to add product to cart: ${response.message}")
                        }
                    }
                }
            })
        }

        // Sửa lại hàm getCart để trả về List<CartItemData>? thay vì List<OrderData>?
        fun getCart(userName: String, onResult: (Boolean, List<CartItemData>?, String) -> Unit) {
            val url = "http://10.0.2.2:8080/cart/get?userName=$userName"
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Handler(Looper.getMainLooper()).post {
                        onResult(false, null, "Failed to connect to server: ${e.message}")
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val json = response.body?.string()
                    if (response.isSuccessful && json != null) {
                        // Parse JSON thành List<CartItemData>
                        val items = Gson().fromJson(json, Array<CartItemData>::class.java).toList()
                        Handler(Looper.getMainLooper()).post {
                            onResult(true, items, "Success")
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            onResult(false, null, "Failed to fetch cart items: ${response.message}")
                        }
                    }
                }
            })
        }


    }
}
