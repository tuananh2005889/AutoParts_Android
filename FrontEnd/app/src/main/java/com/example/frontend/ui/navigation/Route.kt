package com.example.frontend.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Route(val route: String, val icon: ImageVector? = null) {
    object Splash: Route("splash", null)
    object Login: Route("login", null)
    object Signup : Route("signup", null)

    object Home : Route("home", Icons.Default.Home)
    object Cart : Route("cart", Icons.Default.ShoppingCart)
    object Profile : Route("profile", Icons.Default.Person)
    object ForgotPassword : Route("forgotPassword", null)

    object DetailProduct: Route(route = "detailProduct/{productId}", null){
        fun createRouteById(productId: Long): String {
            return "detailProduct/${productId}"
        }
    }
}