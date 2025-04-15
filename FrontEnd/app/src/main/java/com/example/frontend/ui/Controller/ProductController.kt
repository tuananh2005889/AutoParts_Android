package com.example.frontend.ui.Controller

import java.io.FileOutputStream
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.example.frontend.data.model.ProductData
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.File
import java.io.IOException
import okhttp3.MultipartBody

class ProductController {
    companion object {
        fun uriToFile(uri: Uri, context: Context): File? {
            return try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}")
                val outputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)
                inputStream?.close()
                outputStream.close()
                file
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

        fun uploadImage(file: File, onResult: (Boolean, String) -> Unit) {
            val client = OkHttpClient()
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.name, requestBody)
                .build()

            val request = Request.Builder()
                .url("http://10.0.2.2:8080/upload/image")
                .post(multipartBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    onResult(false, e.message ?: "Upload error")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val result = response.body?.string() ?: ""
                        onResult(true, result)
                    } else {
                        onResult(false, "Upload failed")
                    }
                }
            })
        }

        // Hàm confirmAddProduct trong Controller
        fun confirmAddProduct(
            context: Context,
            product: ProductData,
            imageUri: Uri?,
            onResult: (Boolean, String) -> Unit
        ) {
            if (imageUri == null) {
                onResult(false, "Please select an image")
                return
            }

            val file = uriToFile(imageUri, context)
            if (file == null) {
                onResult(false, "Cannot process selected image")
                return
            }

            uploadImage(file) { success, uploadedUrl ->
                if (success) {
                    val productWithImage = product.copy(imageUrlList = listOf(uploadedUrl))

                    addProduct(productWithImage) { addSuccess, msg ->
                        onResult(addSuccess, msg)
                    }
                } else {
                    onResult(false, uploadedUrl)
                }
            }
        }

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

        fun getAllProducts(onResult: (Boolean, List<ProductData>?, String) -> Unit) {
            val url = "http://10.0.2.2:8080/product/all"
            val client = OkHttpClient()

            val request = Request.Builder().url(url).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Handler(Looper.getMainLooper()).post{
                        onResult(false, null, "Failed to connect to server")
                    }
                    }

                override fun onResponse(call: Call, response: Response) {
                    val json = response.body?.string()
                    if (response.isSuccessful && json != null) {
                        val products = Gson().fromJson(json, Array<ProductData>::class.java).toList()
                        Handler(Looper.getMainLooper()).post {
                            onResult(true, products, "Success")
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            onResult(false, null, "Failed to fetch products")
                        }
                    }
                }
            })
        }
    }
}