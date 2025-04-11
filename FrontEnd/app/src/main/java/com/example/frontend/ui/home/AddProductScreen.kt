package com.example.frontend.ui.home

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.frontend.Controller.ProductController
import com.example.frontend.Controller.ProductController.Companion.getAllProducts
import com.example.frontend.data.model.ProductData
import com.example.frontend.ui.compon.HomeChange
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun AddProductScreen(navController: NavController) {
    // Form state
    var showForm by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var compatibleVehicles by remember { mutableStateOf("") }
    var yearOfManufacture by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("") }
    var material by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var discount by remember { mutableStateOf("") }
    var warranty by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Image state: lưu URI đã chọn và URL nhận từ backend
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var imageUrl by remember { mutableStateOf("") }

    // Danh sách sản phẩm hiện có
    val productList = remember { mutableStateListOf<ProductData>() }

    val context = LocalContext.current

    // chọn ảnh
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    fun confirmAddProduct() {
        val productIds = generateProductId()
        val product = ProductData(
            productId = productIds,
            name = name,
            brand = brand,
            description = description,
            category = category,
            compatibleVehicles = compatibleVehicles,
            yearOfManufacture = yearOfManufacture.toIntOrNull(),
            size = size,
            material = material,
            weight = weight.toDoubleOrNull(),
            discount = discount.toDoubleOrNull(),
            warranty = warranty,
            price = price.toDoubleOrNull(),
            quantity = quantity.toIntOrNull()
        )

        ProductController.confirmAddProduct(context, product, selectedImageUri) { success, message ->
            if (success) {
                showForm = false
                name = ""
                brand = ""
                description = ""
                category = ""
                compatibleVehicles = ""
                yearOfManufacture = ""
                size = ""
                material = ""
                weight = ""
                selectedImageUri = null
                imageUrl = ""
                discount = ""
                warranty = ""
                price = ""
                quantity = ""
                errorMessage = ""

                getAllProducts { successReload, products, _ ->
                    if (successReload && products != null) {
                        productList.clear()
                        productList.addAll(products)
                    }
                }
            } else {
                errorMessage = message
            }
        }
    }


    LaunchedEffect(Unit) {
        getAllProducts { success, products, message ->
            if (success && products != null) {
                productList.clear()
                productList.addAll(products)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF30393E))
                .padding(16.dp)
                .padding(bottom = 80.dp) // chừa chỗ cho HomeChange
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Button(
                onClick = { showForm = !showForm },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF15D43))
            ) {
                Text(if (showForm) "Close" else "Add product", color = Color.White)
            }

            AnimatedVisibility(visible = showForm) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .heightIn(max = 500.dp) // giới hạn chiều cao, dễ thao tác hơn
                ) {
                    item {
                        TextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Product Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = brand,
                            onValueChange = { brand = it },
                            label = { Text("Brand") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = category,
                            onValueChange = { category = it },
                            label = { Text("Category") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = compatibleVehicles,
                            onValueChange = { compatibleVehicles = it },
                            label = { Text("Compatible Vehicles") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = yearOfManufacture,
                            onValueChange = { yearOfManufacture = it },
                            label = { Text("Year of Manufacture") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = size,
                            onValueChange = { size = it },
                            label = { Text("Size") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = material,
                            onValueChange = { material = it },
                            label = { Text("Material") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = weight,
                            onValueChange = { weight = it },
                            label = { Text("Weight") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = discount,
                            onValueChange = { discount = it },
                            label = { Text("Discount") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = warranty,
                            onValueChange = { warranty = it },
                            label = { Text("Warranty") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = price,
                            onValueChange = { price = it },
                            label = { Text("Price") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextField(
                            value = quantity,
                            onValueChange = { quantity = it },
                            label = { Text("Quantity") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        // Nút chọn ảnh
                        Button(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
                        ) {
                            Text("Select Image", color = Color.White)
                        }
                        // Hiển thị ảnh đã chọn
                        selectedImageUri?.let { uri ->
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = "Selected Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(top = 8.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                        if (errorMessage.isNotEmpty()) {
                            Text(
                                text = errorMessage,
                                color = Color.Red,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        Button(
                            onClick = { confirmAddProduct() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF15D43))
                        ) {
                            Text("Confirm", color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Product list",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(productList) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFCEAE5))
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text("Name: ${product.name}", color = Color.Black)
                            Text("Brand: ${product.brand}", color = Color.Black)
                            Text("Category: ${product.category}", color = Color.Black)
                            Text("Price: ${product.price}", color = Color.Black)
                        }
                    }
                }
            }
        }

        HomeChange(
            navController = navController,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}
fun generateProductId(): String {
    val randomDigits = (1000..9999).random()
    return "PR$randomDigits"
}
