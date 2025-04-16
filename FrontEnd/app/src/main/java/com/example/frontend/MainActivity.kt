package com.example.frontend


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frontend.ViewModel.UserViewModel
import com.example.frontend.ui.LoginSignUp.LoginScreen
import com.example.frontend.ui.LoginSignUp.SignUpScreen
import com.example.frontend.ui.cart.CartScreen
import com.example.frontend.ui.home.AddProductScreen
import com.example.frontend.ui.home.HomePageScreen
import com.example.frontend.ui.theme.FrontEndTheme


class MainActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            FrontEndTheme {
                AppNavigator(userViewModel = userViewModel)
            }
        }
    }
}

@Composable
fun AppNavigator(userViewModel: UserViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, userViewModel) }
        composable("signup") { SignUpScreen(navController) }
        composable("homepage") { HomePageScreen(navController, userViewModel) }
        composable("addProduct") { AddProductScreen(navController) }
        composable("cart") { CartScreen(navController, userViewModel) }
    }
}

