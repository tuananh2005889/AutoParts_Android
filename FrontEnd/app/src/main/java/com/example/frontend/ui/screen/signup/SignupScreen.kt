package com.example.frontend.ui.screen.signup

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.data.model.UserData
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

@SuppressLint("RestrictedApi")
@Composable
fun SignupScreen(
    onBackToLogin: ()->Unit,
    onSignupSuccess: ()->Unit,
    ) {
    var userName by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var gmail by remember { mutableStateOf("") }
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
        Box(modifier = Modifier.height(200.dp)) {
            Image(
                painter = painterResource(id = R.drawable.login2),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(40.dp)
                    .fillMaxHeight()
                    .offset(x = (30).dp, y = (20).dp)
                    .graphicsLayer(
                        scaleY = 1.3f
                    )
            )
            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = "Car Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .offset(x = (40).dp)
                    .shadow(8.dp, shape = RoundedCornerShape(10.dp))

            )
            Text(
                text = "SIGN UP",
                fontSize = 72.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (55).dp, x = (-20).dp)
                    .shadow(8.dp, shape = RoundedCornerShape(10.dp))
            )
        }


        Spacer(modifier = Modifier.height(120.dp))

        SignUpField("Enter your user name", value = userName, onValueChange = { userName = it })
        Spacer(modifier = Modifier.height(10.dp))

        SignUpField("Enter your full name", value = fullName, onValueChange = { fullName = it })
        Spacer(modifier = Modifier.height(10.dp))

        SignUpField("Enter your gmail", value = gmail, onValueChange = { gmail = it })
        Spacer(modifier = Modifier.height(10.dp))

        SignUpField("Enter your phone", value = phone, onValueChange = { phone = it })
        Spacer(modifier = Modifier.height(10.dp))

        SignUpField("Enter your password", value = password, isPassword = true, onValueChange = { password = it })
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val user = UserData(
                    userId = null,
                    userName = userName,
                    fullName = fullName,
                    gmail = gmail,
                    phone = phone,
                    password = password,
                    isActive = true,
                    avatarUrl = null
                )
                signUpAccount(user) { success, message ->
                    if (success) {
                        onSignupSuccess()
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
        TextButton(onClick = { onBackToLogin() }) {
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
            Handler(Looper.getMainLooper()).post {
                onResult(false, "Failed to connect to server")
            }
        }
        override fun onResponse(call: Call, response: Response) {
            Handler(Looper.getMainLooper()).post {
                if (response.isSuccessful) {
                    onResult(true, "Sign up successful!")
                } else {
                    onResult(false, "Sign up failed")
                }
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
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewSignUpScreen() {
//    val navController = rememberNavController()
//    SignupScreen(navController)
//}
//
