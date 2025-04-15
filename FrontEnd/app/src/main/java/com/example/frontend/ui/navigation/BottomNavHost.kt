package com.example.frontend.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.frontend.ui.screen.cart.CartScreen
import com.example.frontend.ui.screen.home.DetailProductScreen
import com.example.frontend.ui.screen.home.HomeScreenContent
import com.example.frontend.ui.screen.profile.ProfileScreen

@Composable
fun BottomNavHost(bottomNavController : NavHostController, rootNavController: NavController, innerPadding: PaddingValues){
    NavHost(
        navController = bottomNavController,
        startDestination = Route.Home.route
    ){
        composable(Route.Home.route) {
            HomeScreenContent(
                innerPadding = innerPadding,
                onProductClick = { id ->
                    bottomNavController.navigate(Route.DetailProduct.createRouteById(id))
                },

            )
        }

        composable(
            route = Route.DetailProduct.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: return@composable
            DetailProductScreen(productId = productId, innerPadding = innerPadding)
        }

        composable(Route.Profile.route) { ProfileScreen(
            onLogout = {
                rootNavController.navigate(Route.Login.route)
            }
        )}
        composable(Route.Cart.route) { CartScreen()}

    }

}