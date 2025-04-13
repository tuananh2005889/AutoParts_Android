package com.example.frontend.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.frontend.ui.screen.cart.CartScreen
import com.example.frontend.ui.screen.home.HomeScreenContent
import com.example.frontend.ui.screen.profile.ProfileScreen

@Composable
fun BottomNavHost(navHostController: NavHostController, innerPadding: PaddingValues){
    NavHost(
        navController = navHostController,
        startDestination = Route.Home.route
    ){
        composable(Route.Home.route) { HomeScreenContent(innerPadding) }
        composable(Route.Profile.route) { ProfileScreen()}
        composable(Route.Cart.route) { CartScreen()}

    }

}