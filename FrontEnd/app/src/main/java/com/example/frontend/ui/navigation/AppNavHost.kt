package com.example.frontend.ui.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.frontend.data.model.LoginData
import com.example.frontend.ui.screen.login.LoginScreen
import com.example.frontend.ui.screen.signup.SignupScreen
import com.example.frontend.ui.screen.home.HomeScreen
import com.example.frontend.ui.screen.login.LoginViewModel
import com.google.firebase.firestore.auth.User

@Composable
fun AppNavHost(
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val isLoggedIn by loginViewModel.isLoggedIn
    val isInitialCheckDone by loginViewModel.isInitialCheckDone



    NavHost(
        navController = navController,
        startDestination = Route.Splash.route
    ) {
        composable(Route.Splash.route){
            if (isInitialCheckDone) {
                LaunchedEffect(navController) {
                    if (isLoggedIn) {
                        navController.navigate(Route.Home.route) {
                            popUpTo("splash") { inclusive = true }
                        }
                    } else {
                        navController.navigate(Route.Login.route) {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
            } else {
                CircularProgressIndicator(modifier = Modifier.size(250.dp))
        }
        }
        // Auth Screens
        composable(Route.Login.route) {
            LoginScreen(
                loginViewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Route.Home.route) {
                        popUpTo(Route.Login.route){ //default inclusive is false
                            inclusive = true
                        }
                    }
                },
                onSignupClick = {
                    navController.navigate(Route.Signup.route)
                },
//                onLoginClick = {
//                    val user = LoginData(
//                        userName = loginViewModel.loginTextFieldState.value.userName.toString(),
//                        password = loginViewModel.loginTextFieldState.value.password.toString()
//                    )
//
//                    loginViewModel.login(user)
//                }
            )
        }
        composable(Route.Signup.route) {
            SignupScreen(
                onBackToLogin = {
                    navController.navigate(Route.Login.route)
                },
                onSignupSuccess = {
                        navController.navigate(Route.Login.route) {
                            popUpTo("signup") { inclusive = true }
                    }
                }
            )
        }
        // Main Screens
        composable(Route.Home.route) {
            HomeScreen(
                rootNavController = navController,
                loginViewModel = loginViewModel
            )
        }

    }
}
