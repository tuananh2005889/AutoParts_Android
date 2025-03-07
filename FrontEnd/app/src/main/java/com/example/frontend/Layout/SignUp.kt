package com.example.frontend.Layout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R

class MainActivitys : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen()
        }
    }
}

@Composable
fun SignUpScreen() {
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
                    .offset(x=(320).dp,y =(20).dp)
                    .graphicsLayer(
                        scaleY = 1.3f
                    )
//                    .border(2.dp, Color.Black, shape =RoundedCornerShape(10.dp))
//                    .shadow(8.dp, shape = RoundedCornerShape(10.dp))

            )
            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = "Car Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .offset(x=(-50).dp)
//                    .border(2.dp, Color.Black, shape =RoundedCornerShape(10.dp))
                    .shadow(8.dp, shape = RoundedCornerShape(10.dp))

            )
            Text(
                text = "SIGN UP",
                fontSize = 72.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.BottomCenter)
                    .offset(y=(55).dp, x = (-20).dp)
                    .shadow(8.dp, shape = RoundedCornerShape(10.dp))
            )
        }
        Spacer(modifier = Modifier.height(80.dp))
        SignUpField("Enter your user name",)
        Spacer(modifier = Modifier.height(10.dp))
        SignUpField("Enter your full name", isPassword = true)
        Spacer(modifier = Modifier.height(10.dp))
        SignUpField("Enter your email", isPassword = true)
        Spacer(modifier = Modifier.height(10.dp))
        SignUpField("Enter your phone", isPassword = true)
        Spacer(modifier = Modifier.height(10.dp))
        SignUpField("Enter your password", isPassword = true)
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF15D43)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.width(200.dp)
        ) {
            Text(text = "Sign Up", fontSize = 18.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "You have an account? Sign In now", color = Color.White, fontSize = 12.sp)
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

@Composable
fun SignUpField(placeholder: String, isPassword: Boolean = false, modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
    TextField(
        value = "",
        onValueChange = {},
        placeholder = { Text(text = placeholder, color = Color.Gray, fontSize = 14.sp) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .width(300.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(16.dp)

            )
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {
    SignUpScreen()
}
