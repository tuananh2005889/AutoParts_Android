package com.example.frontend

import AddProductScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frontend.ui.LoginSignUp.LoginScreen
import com.example.frontend.ui.LoginSignUp.SignUpScreen
import com.example.frontend.ui.theme.FrontEndTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontEndTheme {
                AppNavigator()
            }


        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("addProduct") { AddProductScreen(navController) }
    }
}

