package com.example.frontend.Layout
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen()
        }
    }
}

@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF30393E)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Box(modifier = Modifier.height(200.dp)) {
//            Image(
//                painter = painterResource(id = R.drawable.car_image),
//                contentDescription = "Car Image",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(180.dp)
//            )
            Text(
                text = "LOGIN",
                fontSize = 72.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        LoginField("Enter your user name")
        Spacer(modifier = Modifier.height(10.dp))
        LoginField("Enter your password", isPassword = true)
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { /* Handle login */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF15D43)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.width(200.dp)
        ) {
            Text(text = "LogIn", fontSize = 18.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "You don’t have an account? Register now", color = Color.White, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Use other methods", color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(10.dp))
//        Image(
//            painter = painterResource(id = R.drawable.n),
//            contentDescription = "Google Login",
//            modifier = Modifier.size(40.dp)
//        )
    }
}

@Composable
fun LoginField(placeholder: String, isPassword: Boolean = false) {
    TextField(
        value = "",
        onValueChange = {},
        placeholder = { Text(text = placeholder, color = Color.Gray) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
//        colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier

            .fillMaxHeight(0.1f)
            .clip(RoundedCornerShape(16.dp))
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen()
}
