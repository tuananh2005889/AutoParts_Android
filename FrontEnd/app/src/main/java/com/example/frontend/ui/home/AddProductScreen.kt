import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.frontend.data.model.ProductData
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

@Composable
fun AddProductScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var category_id by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var yearOfManufacture by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("") }
    var material by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }
    var discount by remember { mutableStateOf("") }
    var warranty by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFF30393E)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        listOf(
            "Product Name" to name,
            "Description" to description,
            "Price" to price,
            "Quantity" to quantity,
            "Category" to category_id,
            "Brand" to brand,
            "Year of Manufacture" to yearOfManufacture,
            "Size" to size,
            "Material" to material,
            "Weight" to weight,
            "Image URL" to image,
            "Discount" to discount,
            "Warranty" to warranty
        ).forEach { (label, value) ->
            TextField(
                value = value,
                onValueChange = { newValue ->
                    when (label) {
                        "Product Name" -> name = newValue
                        "Description" -> description = newValue
                        "Price" -> price = newValue
                        "Quantity" -> quantity = newValue
                        "Category" -> category_id = newValue
                        "Brand" -> brand = newValue
                        "Year of Manufacture" -> yearOfManufacture = newValue
                        "Size" -> size = newValue
                        "Material" -> material = newValue
                        "Weight" -> weight = newValue
                        "Image URL" -> image = newValue
                        "Discount" -> discount = newValue
                        "Warranty" -> warranty = newValue
                    }
                },
                label = { Text(label) },
                keyboardOptions = if (label in listOf("Price", "Quantity", "Year of Manufacture", "Weight", "Discount"))
                    KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                else KeyboardOptions.Default,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                val product = ProductData(
                    name = name,
                    description = description,
                    price = price.toDoubleOrNull(),
                    quantity = quantity.toIntOrNull(),
                    category_id = category_id,
                    brand = brand,
                    yearOfManufacture = yearOfManufacture.toIntOrNull(),
                    size = size,
                    material = material,
                    weight = weight.toDoubleOrNull(),
                    image = image,
                    discount = discount.toDoubleOrNull(),
                    warranty = warranty
                )
                sendProductToServer(product) { success, message ->
                    if (success) {
                        navController.navigate("productList")
                    } else {
                        errorMessage = message
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF15D43))
        ) {
            Text(text = "Add Product", color = Color.White)
        }
    }
}

fun sendProductToServer(product: ProductData, onResult: (Boolean, String) -> Unit) {
    val url = "http://10.0.2.2:8080/product/add"
    val client = OkHttpClient()
    val json = Gson().toJson(product)
    val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
    val request = Request.Builder().url(url).post(requestBody).build()



    client.newCall(request).enqueue(object : Callback {
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
                    Log.d("API_REQUEST", "JSON gửi lên: $json")
                    onResult(false, "Failed to add product")
                }
            }
        }
    })
}