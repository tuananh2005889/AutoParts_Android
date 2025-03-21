package com.example.frontend.LoginSignUp

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.frontend.R
import com.example.frontend.dataClass.UserData
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

@SuppressLint("RestrictedApi")
@Composable
fun SignUpScreen(navController: NavHostController) {
    var userName by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF30393E)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(90.dp))

        Text(text = "SIGN UP", fontSize = 32.sp, color = Color.White, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(50.dp))

        SignUpField("Enter your user name", value = userName, onValueChange = { userName = it })
        Spacer(modifier = Modifier.height(10.dp))

        SignUpField("Enter your full name", value = fullName, onValueChange = { fullName = it })
        Spacer(modifier = Modifier.height(10.dp))

        SignUpField("Enter your email", value = email, onValueChange = { email = it })
        Spacer(modifier = Modifier.height(10.dp))

        SignUpField("Enter your phone", value = phone, onValueChange = { phone = it })
        Spacer(modifier = Modifier.height(10.dp))

        SignUpField("Enter your password", value = password, isPassword = true, onValueChange = { password = it })
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val user = UserData(userName, fullName, email, phone, password)
                signUpAccount(user) { success, message ->
                    if (success) {
                        navController.navigate("login")
                    } else {
                        errorMessage = message
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF15D43)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.width(200.dp)
        ) {
            Text(text = "Sign Up", fontSize = 18.sp, color = Color.White)
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))
        TextButton(onClick = { navController.navigate("login") }) {
            Text(text = "You have an account? Sign In now", color = Color.White, fontSize = 12.sp)
        }
    }
}

fun signUpAccount(user: UserData, onResult: (Boolean, String) -> Unit) {
    val url = "http://10.0.2.2:8080/auth/signup"
    val client = OkHttpClient()
    val json = Gson().toJson(user)
    val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

    val request = Request.Builder().url(url).post(requestBody).build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onResult(false, "Failed to connect to server")
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                onResult(true, "Sign up successful!")
            } else {
                onResult(false, "Sign up failed")
            }
        }
    })
}

@Composable
fun SignUpField(placeholder: String, value: String, onValueChange: (String) -> Unit, isPassword: Boolean = false) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder, color = Color.Gray, fontSize = 14.sp) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(300.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(16.dp))
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {
    val navController = rememberNavController()
    SignUpScreen(navController)
}
