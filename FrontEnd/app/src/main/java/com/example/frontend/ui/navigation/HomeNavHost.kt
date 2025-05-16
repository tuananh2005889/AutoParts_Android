package com.example.frontend.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.frontend.ui.screen.cart.CartScreen
import com.example.frontend.ViewModel.CartViewModel
import com.example.frontend.ui.screen.home.DetailProductScreen
import com.example.frontend.ui.screen.home.HomeScreenContent
import com.example.frontend.ViewModel.LoginViewModel
import com.example.frontend.ui.screen.profile.ProfileScreen
import com.example.frontend.ViewModel.ProfileViewModel
import com.example.frontend.ui.screen.order.OrderScreen

/**
 * Root navigation host for the bottom‑navigation area that sits inside [HomeScreen].
 *
 * The composable now correctly waits until a non‑null `userName` is loaded from DataStore
 * before navigating to the Profile screen, preventing repeated 404 calls when the value
 * was previously the literal string "null".
 */
@Composable
fun HomeNavHost(
    loginViewModel: LoginViewModel,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    bottomNavController: NavHostController,
    rootNavController: NavController,
    innerPadding: PaddingValues,
    onShowSnackBar: (String) -> Unit,
) {

    var userName by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        userName = loginViewModel.getUserName()              // suspend fun – safe here
        Log.d("HomeNavHost", "Loaded userName = $userName")
    }

    NavHost(
        navController = bottomNavController,
        startDestination = Route.Home.route
    ) {
        /* ---------------- Home ---------------- */
        composable(Route.Home.route) {
            HomeScreenContent(
                innerPadding = innerPadding,
                onProductClick = { id ->
                    bottomNavController.navigate(Route.DetailProduct.createRouteById(id))
                },
                onShowSnackBar = onShowSnackBar
            )
        }

        /* ------------- Product detail ------------- */
        composable(
            route = Route.DetailProduct.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: return@composable
            val productIdLong = productId.toLongOrNull() ?: return@composable

            DetailProductScreen(
                productId = productIdLong,
                innerPadding = innerPadding,
                clickBack = { bottomNavController.popBackStack() }
            )
        }

        /* ---------------- Profile ---------------- */
        composable(Route.Profile.route) {
            // Render Profile only when we have a real userName
            userName?.let { name ->
                Log.d("HomeNavHost", "Navigate to Profile → userName = $name")
                ProfileScreen(
                    userName = name,
                    profileViewModel = profileViewModel,
                    onLogout = {
                        loginViewModel.logout()
                        rootNavController.navigate(Route.Login.route) {
                            popUpTo(Route.Login.route) { inclusive = true }
                        }
                        loginViewModel.setLoginTextField()
                    }
                )
            } ?: Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        /* ---------------- Cart ---------------- */
        composable(Route.Cart.route) {
            CartScreen(navController = bottomNavController)
        }
        composable(route = Route.Order.route){
            OrderScreen()
        }
    }
}
