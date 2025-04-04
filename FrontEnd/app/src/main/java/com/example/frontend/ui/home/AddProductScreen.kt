import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.items


import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.ui.unit.dp

import androidx.navigation.NavController

import com.example.frontend.Controller.ProductController.Companion.addProduct
import com.example.frontend.data.model.ProductData

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.frontend.Controller.ProductController.Companion.getAllProducts

@Composable
fun AddProductScreen(navController: NavController) {
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
    var img by remember { mutableStateOf("") }
    var discount by remember { mutableStateOf("") }
    var warranty by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val productList = remember { mutableStateListOf<ProductData>() }

    LaunchedEffect(Unit) {
        getAllProducts { success, products, message ->
            if (success && products != null) {
                productList.clear()
                productList.addAll(products)
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF30393E))
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = { showForm = !showForm },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF15D43))
        ) {
            Text(if (showForm) "Close" else "Add product", color = Color.White)
        }

        AnimatedVisibility(visible = showForm) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .verticalScroll(scrollState)

            ) {
                listOf(
                    "Product Name" to name,
                    "Brand" to brand,
                    "Description" to description,
                    "Category" to category,
                    "Compatible Vehicles" to compatibleVehicles,
                    "Year of Manufacture" to yearOfManufacture,
                    "Size" to size,
                    "Material" to material,
                    "Weight" to weight,
                    "Image" to img,
                    "Discount" to discount,
                    "Warranty" to warranty,
                    "Price" to price,
                    "Quantity" to quantity,
                ).forEach { (label, value) ->
                    TextField(
                        value = value,
                        onValueChange = {
                            when (label) {
                                "Product Name" -> name = it
                                "Brand" -> brand = it
                                "Description" -> description = it
                                "Category" -> category = it
                                "Compatible Vehicles" -> compatibleVehicles = it
                                "Year of Manufacture" -> yearOfManufacture = it
                                "Size" -> size = it
                                "Material" -> material = it
                                "Weight" -> weight = it
                                "Image" -> img = it
                                "Discount" -> discount = it
                                "Warranty" -> warranty = it
                                "Price" -> price = it
                                "Quantity" -> quantity = it
                            }
                        },
                        label = { Text(label) },
                        keyboardOptions = if (label in listOf(
                                "Price",
                                "Quantity"
                            )
                        ) KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        else KeyboardOptions.Default,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
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
                    onClick = {
                        val product = ProductData(
                            name = name,
                            brand = brand,
                            description = description,
                            category = category,
                            compatibleVehicles = compatibleVehicles,
                            yearOfManufacture = yearOfManufacture.toIntOrNull(),
                            size = size,
                            material = material,
                            weight = weight.toDoubleOrNull(),
                            image = img,
                            discount = discount.toDoubleOrNull(),
                            warranty = warranty,
                            price = price.toDoubleOrNull(),
                            quantity = quantity.toIntOrNull(),
                        )
                        addProduct(product) { success, message ->
                            if (success) {
                                getAllProducts { successReload, products, _ ->
                                    if (successReload && products != null) {
                                        productList.clear()
                                        productList.addAll(products)
                                    }
                                }
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
                                img = ""
                                discount = ""
                                warranty = ""
                                price = ""
                                quantity = ""
                            } else {
                                errorMessage = message
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF15D43))
                ) {
                    Text("Confirm", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Product list", color = Color.White, style = MaterialTheme.typography.titleMedium)
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
                        Text("Name product: ${product.name}", color = Color.Black)
                        Text("Brand: ${product.brand}", color = Color.Black)
                        Text("Category: ${product.category}", color = Color.Black)
                        Text(
                            "Compatible Vehicles: ${product.compatibleVehicles}",
                            color = Color.Black
                        )
                        Text(
                            "Year of Manufacture: ${product.yearOfManufacture}",
                            color = Color.Black
                        )
                        Text("Size: ${product.size}", color = Color.Black)
                        Text("Material: ${product.material}", color = Color.Black)
                        Text("Weight: ${product.weight}", color = Color.Black)
                        Text("Image: ${product.image ?: "?"}", color = Color.Black)
                        Text("Discount: ${product.discount}", color = Color.Black)
                        Text("Warranty: ${product.warranty}", color = Color.Black)
                        Text("Price: ${product.price}", color = Color.Black)
                        Text("Quantity: ${product.quantity}", color = Color.Black)
                        Text("Description: ${product.description}", color = Color.DarkGray)
                    }
                }
            }
        }
    }
}


