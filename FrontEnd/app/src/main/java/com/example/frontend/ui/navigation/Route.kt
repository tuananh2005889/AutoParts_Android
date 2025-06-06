package com.example.frontend.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Route(val route: String, val icon: ImageVector? = null) {
    object Splash: Route("splash", null)
    object Login: Route("login", null)
    object Signup : Route("signup", null)

    object Home : Route("Home", Icons.Default.Home)
    object Cart : Route("Cart", Icons.Default.ShoppingCart)
    object Order: Route("Order", Icons.Default.AllInbox)
    object Profile : Route("Profile", Icons.Default.Person)
//    object ProfileGraph: Route("ProfileGraph", Icons.Default.Person)
    object ForgotPassword : Route("forgotPassword", null)

    object DetailProduct: Route(route = "detailProduct/{productId}", null){
        fun createRouteById(productId: Long): String {
            return "detailProduct/${productId}"
        }
    }

    object AwaitingConfirmation: Route("awaitingConfirmation")
    object AwaitingShipment: Route("awaitingShipment")
    object InTransit: Route("inTransit")
    object Delivered: Route("delivered")
    object Category : Route("category", Icons.Default.AllInbox)


    object ProductList : Route("productList/{category}", null) {
        fun createRoute(category: String) = "productList/$category"
    }
}