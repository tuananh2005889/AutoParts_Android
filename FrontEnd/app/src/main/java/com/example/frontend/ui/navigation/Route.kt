package com.example.frontend.ui.navigation

sealed class Route(val route: String) {
    object Login: Route("login")
    object Signup : Route("signup")

    object Home : Route("home")
    object Cart : Route("cart")
    object User : Route("user")
}