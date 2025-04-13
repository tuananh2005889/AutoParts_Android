package com.example.frontend.ui.navigation

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frontend.ui.screen.login.LoginScreen
import com.example.frontend.ui.screen.signup.SignupScreen
import com.example.frontend.ui.screen.home.HomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    isLoggedIn: MutableState<Boolean>
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn.value) Route.Home.route else Route.Login.route
    ) {
        // Auth Screens
        composable(Route.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    isLoggedIn.value = true
                    navController.navigate(Route.Home.route) {
                        popUpTo(Route.Login.route){ //default inclusive is false
                            inclusive = true
                        }
                    }
                },
                onSignupClick = {
                    navController.navigate(Route.Signup.route)
                }
            )
        }
        composable(Route.Signup.route) {
            SignupScreen(
                onBackToLogin = {
                    navController.navigate(Route.Login.route)
                },
                onSignupSuccess = {
//                    Handler(Looper.getMainLooper()).post {
                        navController.navigate(Route.Login.route) {
                            popUpTo("signup") { inclusive = true }
//                        }
                    }
                }
            )
        }
        // Main Screens
        composable(Route.Home.route) { HomeScreen() }

    }
}
