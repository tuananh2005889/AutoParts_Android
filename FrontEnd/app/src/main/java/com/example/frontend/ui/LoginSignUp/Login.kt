package com.example.frontend.ui.LoginSignUp

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.frontend.R
import com.example.frontend.ViewModel.UserViewModel
import com.example.frontend.data.UserPreferences.getUserData
import com.example.frontend.data.UserPreferences.saveUserData
import com.example.frontend.data.model.LoginData
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            LoginScreen()
//        }
//    }
//}

@Composable
fun LoginScreen(navController: NavHostController, userViewModel: UserViewModel) {
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
//    val fullName by remember { mutableStateOf("") }
//    val gmail by remember { mutableStateOf("") }
//    val phone by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var loginSuccess by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF30393E)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
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
                text = "LOGIN",
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
        LoginField("Enter your user name", value = userName, onValueChange = {userName = it})
        Spacer(modifier = Modifier.height(10.dp))
        LoginField("Enter your password", isPassword = true, value = password, onValueChange = {password = it})
        Spacer(modifier = Modifier.height(20.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(10.dp))
        }
        Button(
            onClick = {
                val user = LoginData(userName, password)


                loginAccount(user) { success, message ->
                    if (success) {

                        val loggedInUser = LoginData(
                            userName = user.userName,
                            password = user.password,
                        )
                        saveUserData(context, loggedInUser)
                        val savedUser = getUserData(context)
                        Log.d("Login", "Saved user: $savedUser")
                        userViewModel.setCurrentUser(loggedInUser)

                        navController.navigate("homepage") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        errorMessage = message
                    }
                }

            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF15D43)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.width(200.dp)
        ) {
            Text(text = "LogIn", fontSize = 18.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(10.dp))
        TextButton(onClick = { navController.navigate("signup") }) {
            Text(
                text = "You don’t have an account? Register now",
                color = Color.White,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Use other methods", color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Image(
            painter = painterResource(id = R.drawable.logogoogle),
            contentDescription = "Google Login",
            modifier = Modifier.size(50.dp)
        )
    }
}

fun loginAccount(user: LoginData, onResult: (Boolean, String) -> Unit) {
    val url = "http://10.0.2.2:8080/auth/login"
    val client = OkHttpClient()
    val json = Gson().toJson(user)
    val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

    val request = Request.Builder().url(url).post(requestBody).build()

    client.newCall(request).enqueue(object: Callback {
        override fun onFailure(call: Call, e: IOException) {
             Handler(Looper.getMainLooper()).post {
                    onResult(false,"Failed to connect to server")
             }
        }

        override fun onResponse(call: Call, response: Response) {
            Handler(Looper.getMainLooper()).post {
                if (response.isSuccessful) onResult(true, "Login successful")
                else onResult(false, "User name or password incorrect")
            }
        }
    })
}

@Composable
fun LoginField(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder, color = Color.Gray, fontSize = 14.sp) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(300.dp)
            .height(55.dp)
            .clip(
                RoundedCornerShape(16.dp)
            )
    )
}


//@Preview(showBackground = true)
//@Composable
//fun PreviewLoginScreen() {
//    val navController = rememberNavController()
//    LoginScreen(navController)
//}
