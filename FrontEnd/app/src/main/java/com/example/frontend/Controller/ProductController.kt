package com.example.frontend.Controller


import android.app.VoiceInteractor
import android.os.Handler
import android.os.Looper
import com.example.frontend.dataClass.ProductData
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class ProductController {
    companion object {
        fun addProduct(product: ProductData, onResult: (Boolean, String) -> Unit) {
            val url = "http://10.0.2.2:8080/product/add"
            val client = OkHttpClient()
            val json = Gson().toJson(product)
            val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

            val request = Request.Builder().url(url).post(requestBody).build()

            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Handler(Looper.getMainLooper()).post {
                        onResult(false, "Failed to connect to server")
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    Handler(Looper.getMainLooper()).post {
                        if (response.isSuccessful) {
                            onResult(true, "Product added successfully")
                        } else {
                            onResult(false, "Failed to add product: ${response.message}")
                        }
                    }
                }
            })
        }

    }


}