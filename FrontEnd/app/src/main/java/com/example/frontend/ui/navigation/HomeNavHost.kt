package com.example.frontend.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.frontend.ui.common.AuthPreferencesKeys.userName
import com.example.frontend.ui.screen.cart.CartScreen
import com.example.frontend.ui.screen.cart.CartViewModel
import com.example.frontend.ui.screen.home.DetailProductScreen
import com.example.frontend.ui.screen.home.HomeScreenContent
import com.example.frontend.ui.screen.login.LoginViewModel
import com.example.frontend.ui.screen.profile.ProfileScreen
import com.example.frontend.ui.screen.profile.ProfileViewModel
import kotlin.math.log

@Composable
fun BottomNavHost(
    loginViewModel: LoginViewModel,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    bottomNavController : NavHostController,
    rootNavController: NavController,
    innerPadding: PaddingValues,
    cartViewModel: CartViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
    )
{
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
                onShowSnackBar = onShowSnackBar

            )
        }
        composable(
            route = Route.DetailProduct.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: return@composable
            val productIdLong = productId.toLongOrNull() ?: return@composable
            DetailProductScreen(
                productId =productIdLong , innerPadding = innerPadding,
                clickBack = {bottomNavController.popBackStack()},
            )


        }
        composable(Route.Profile.route) {
            ProfileScreen(
                profileViewModel = profileViewModel,
                onLogout = {
                    loginViewModel.logout()
//                    bottomNavController.popBackStack()
                    rootNavController.navigate(Route.Login.route){
                        popUpTo(Route.Login.route){
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                   loginViewModel.setLoginTextField(userName = "", password = "")
                }
            )
        }
        composable(Route.Cart.route) { CartScreen()}

    }

}