package com.example.frontend

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.frontend.ViewModel.ProductViewModel
import com.example.frontend.dataClass.ProductData


@Composable
fun AddProductScreen(navController: NavHostController, onProductAdded: (ProductData) -> Unit) {
    val productViewModel: ProductViewModel = viewModel()
    val newProduct = productViewModel.newProduct

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add New Product", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newProduct.name,
            onValueChange = { productViewModel.updateNewProductName(it) },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newProduct.brand,
            onValueChange = { productViewModel.updateNewProductBrand(it) },
            label = { Text("Brand") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newProduct.price?.toString() ?: "",
            onValueChange = { productViewModel.updateNewProductPrice(it) },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newProduct.quantity?.toString() ?: "",
            onValueChange = { productViewModel.updateNewProductQuantity(it) },
            label = { Text("Quantity") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                productViewModel.addProduct { addedProduct ->
                    addedProduct?.let {
                        onProductAdded(it)
                        navController.navigate("productDetails/${it.name}") // Navigate to details
                    }
                }
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Product", fontSize = 18.sp)
        }

        if (productViewModel.addProductError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = productViewModel.addProductError!!,
                color = Color.Red,
                fontSize = 14.sp
            )
        }
    }
}