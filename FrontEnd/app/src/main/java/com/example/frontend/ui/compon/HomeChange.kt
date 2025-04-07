package com.example.frontend.ui.compon


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.frontend.ui.home.HomePageScreen

@Composable
fun HomeChange(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") {
            // Trang Home
//            HomePageScreen
//            ()
        }
//        composable("cart") {
//            // Trang giỏ hàng (placeholder)
//            CartScreen()
//        }
//        composable("profile") {
//            // Trang cá nhân (placeholder)
//            ProfileScreen()
//        }
    }
}
