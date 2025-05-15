package com.example.frontend.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
//
//@Composable
//fun BottomNavBar(navController: NavController) {
//    BottomNavigation(
//        backgroundColor = MaterialTheme.colorScheme.background,
//        contentColor = MaterialTheme.colorScheme.onBackground
//    ) {
//        val items = listOf(
//            Route.Home, Route.Cart,Route.Order, Route.Profile
//        )
//        items.forEach { screen ->
//            BottomNavigationItem(
//                icon = { Icon(imageVector = screen.icon ?: Icons.Default.Warning,
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.onSurface
//                ) },
//
//                label = { Text(screen.route) },
//                selected = true,
//                onClick = { navController.navigate(screen.route) }
//            )
//        }
//    }
//}
@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        Route.Home, Route.Cart, Route.Order, Route.Profile
    )

    val currentRoute = navController.currentBackStackEntry?.destination?.route

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        items.forEach { screen ->
            val selected = currentRoute == screen.route

            BottomNavigationItem(
                selected = selected,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationRoute!!) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    androidx.compose.material.Surface(
                        color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.background,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Icon(
                            imageVector = screen.icon ?: Icons.Default.Warning,
                            contentDescription = null,
                            tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = screen.route,
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                },
                alwaysShowLabel = true,
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
