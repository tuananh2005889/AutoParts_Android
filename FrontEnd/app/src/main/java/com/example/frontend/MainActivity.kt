package com.example.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.frontend.ui.navigation.AppNavHost
import com.example.frontend.ui.theme.FrontEndTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontEndTheme {
                val navController = rememberNavController()
                val isLoggedIn = remember { mutableStateOf(false)}
                AppNavHost(navController, isLoggedIn)
//                AppNavigator()
            }


        }
    }
}

//@Composable
//fun AppNavigator() {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = "login") {
//        composable("login") { LoginScreen(navController) }
//        composable("signup") { SignUpScreen(navController) }
//        composable("homepage") { HomePageScreen(navController) }
//        composable("addProduct") { AddProductScreen(navController) }
//    }
//}

