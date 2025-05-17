package com.example.frontend.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.frontend.ViewModel.LoginViewModel
import com.example.frontend.ui.screen.login.LoginScreen
import com.example.frontend.ui.screen.login.ForgotPasswordScreen
import com.example.frontend.ui.screen.signup.SignupScreen
import com.example.frontend.ui.screen.home.HomeScreen

import com.example.frontend.ui.screen.home.DetailProductScreen

import com.example.frontend.ui.navigation.Route
import com.example.frontend.ui.screen.cart.CartScreen
import com.example.frontend.ui.screen.order.OrderScreen
import com.example.frontend.ui.screen.profile.AwaitingConfirmationScreen
import com.example.frontend.ui.screen.profile.AwaitingShipmentScreen
import com.example.frontend.ui.screen.profile.DeliveredScreen
import com.example.frontend.ui.screen.profile.InTransitScreen
import com.example.frontend.ui.screen.profile.ProfileScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    // 1. Các route mà bạn muốn hiển thị BottomNavBar
    val bottomBarRoutes = listOf(
        Route.Home.route,
        Route.Cart.route,
        Route.Order.route,
        Route.Profile.route
    )

    // 2. Lắng nghe trạng thái login
    val isLoggedIn by loginViewModel.isLoggedIn
    val isInitialCheckDone by loginViewModel.isInitialCheckDone

    // 3. Lấy currentRoute để quyết định show/hide bottomBar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        // 4. Chèn bottomBar với điều kiện
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
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
                    Box(
                        Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        CircularProgressIndicator(Modifier.size(48.dp))
                    }
                }
            }

            composable(Route.Login.route) {
                LoginScreen(
                    loginViewModel = loginViewModel,
                    onLoginSuccess = {
                        navController.navigate(Route.Home.route) {
                            popUpTo(Route.Login.route) { inclusive = true }
                        }
                    },
                    onSignupClick = { navController.navigate(Route.Signup.route) },
                    onForgotPasswordClick = { navController.navigate(Route.ForgotPassword.route) }
                )
            }

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

            composable(Route.ForgotPassword.route) {
                ForgotPasswordScreen(
                    onBackToLogin = { navController.popBackStack() }
                )
            }

            // --- Các màn hình có bottom bar ---

            composable(Route.Home.route) {
                HomeScreen(
                    rootNavController = navController,
                )
            }

            composable(Route.Cart.route) {
                CartScreen(
                    navController = navController
                )
            }

            navigation(
                startDestination = Route.Profile.route,
                route = Route.ProfileGraph.route
            ) {
                composable(Route.Profile.route) {
                    val userName by produceState(initialValue = "", loginViewModel) {
                    value = loginViewModel.getUserName().toString()
                }
                    ProfileScreen(
                        userName = userName,
                        onLogout = {
                            // Khi logout thì clear state và back về màn Login
                            loginViewModel.logout()
                            navController.navigate(Route.Login.route) {
                                popUpTo(Route.Home.route) { inclusive = true }
                            }
                        },
                        clickAwaitingConfirmation = {
                            navController.navigate(Route.AwaitingConfirmation.route)
                        },
                        clickAwaitingShipment = {
                            navController.navigate(Route.AwaitingShipment.route)
                        },
                        clickInTransit = {
                            navController.navigate(Route.InTransit.route)
                        },
                        clickDelivered = {
                            navController.navigate(Route.InTransit.route)
                        }
                    )
                }
                composable(Route.AwaitingConfirmation.route) {
                    AwaitingConfirmationScreen()
                }
                composable(Route.AwaitingShipment.route){
                    AwaitingShipmentScreen()
                }
                composable(Route.InTransit.route){
                    InTransitScreen()
                }
                composable(Route.Delivered.route){
                    DeliveredScreen()
                }

            }

            composable(route = Route.Order.route){
                OrderScreen()
            }

            // --- Màn hình detail không có bottom bar ---
            composable(Route.DetailProduct.route) { backStack ->
                val id = backStack.arguments
                    ?.getString("productId")?.toLongOrNull() ?: return@composable
                DetailProductScreen(
                    productId = id,
                    innerPadding = innerPadding,
                    clickBack = { navController.popBackStack() }
                )
            }
        }
    }
}
