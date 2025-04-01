package com.example.frontend.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.dataClass.ProductData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class ProductViewModel : ViewModel() {
    var newProduct by mutableStateOf(ProductData())
        private set

    var addProductSuccess by mutableStateOf(false)
        private set

    var addProductError by mutableStateOf<String?>(null)
        private set

    fun updateNewProduct(product: ProductData) {
        newProduct = product
        addProductSuccess = false
        addProductError = null
    }

    fun updateNewProductName(name: String) {
        newProduct = newProduct.copy(name = name)
    }

    fun updateNewProductBrand(brand: String) {
        newProduct = newProduct.copy(brand = brand)
    }

    fun updateNewProductPrice(price: String) {
        newProduct = newProduct.copy(price = price.toDoubleOrNull())
    }

    fun updateNewProductQuantity(quantity: String) {
        newProduct = newProduct.copy(quantity = quantity.toIntOrNull())
    }

    // Add other update functions for other fields

    fun addProduct(onProductAdded: (ProductData?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val url = "http://10.0.2.2:8080/api/products"
            val client = OkHttpClient()
            val gson = Gson()
            val json = gson.toJson(newProduct)
            val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val addedProduct = gson.fromJson(responseBody, ProductData::class.java)
                    viewModelScope.launch(Dispatchers.Main) {
                        addProductSuccess = true
                        addProductError = null
                        onProductAdded(addedProduct)
                        newProduct = ProductData() // Reset the form
                    }
                } else {
                    viewModelScope.launch(Dispatchers.Main) {
                        addProductSuccess = false
                        addProductError = "Failed to add product: ${response.code}"
                        onProductAdded(null)
                    }
                }
            } catch (e: IOException) {
                viewModelScope.launch(Dispatchers.Main) {
                    addProductSuccess = false
                    addProductError = "Failed to connect to server: ${e.message}"
                    onProductAdded(null)
                }
            }
        }
    }
}