package com.example.frontend.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.frontend.ui.screen.home.HomeScreen
import com.example.frontend.ui.screen.login.LoginScreen
import com.example.frontend.ui.screen.login.LoginViewModel
import com.example.frontend.ui.screen.signup.SignupScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    /* -------- Listen state từ ViewModel -------- */
    val isLoggedIn          by loginViewModel.isLoggedIn       // State<Boolean>
    val isInitialCheckDone  by loginViewModel.isInitialCheckDone

    NavHost(
        navController = navController,
        startDestination = Route.Splash.route
    ) {

        /* ---------- Splash ---------- */
        composable(Route.Splash.route) {
            if (isInitialCheckDone) {
                LaunchedEffect(isLoggedIn) {
                    if (isLoggedIn) {
                        navController.navigate(Route.Home.route) {
                            popUpTo(Route.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Route.Login.route) {
                            popUpTo(Route.Splash.route) { inclusive = true }
                        }
                    }
                }
            } else {
                Box(Modifier.size(250.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }

        /* ---------- Login ---------- */
        composable(Route.Login.route) {
            LoginScreen(
                loginViewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Route.Home.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                },
                onSignupClick = { navController.navigate(Route.Signup.route) }
            )
        }

        /* ---------- Signup ---------- */
        composable(Route.Signup.route) {
            SignupScreen(
                onBackToLogin = { navController.navigate(Route.Login.route) },
                onSignupSuccess = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(Route.Signup.route) { inclusive = true }
                    }
                }
            )
        }

        /* ---------- Home ---------- */
        composable(Route.Home.route) {
            HomeScreen(
                rootNavController = navController,
                loginViewModel    = loginViewModel
            )
        }
    }
}
