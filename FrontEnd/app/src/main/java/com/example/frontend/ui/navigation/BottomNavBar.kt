package com.example.frontend.ui.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.frontend.ui.theme.backgroundDark

@Composable
fun BottomNavBar(navController: NavController) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.surface,
    ) {
        val items = listOf(
            Route.Home, Route.Cart, Route.Profile
        )
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(imageVector = screen.icon ?: Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                ) },

                label = { Text(screen.route) },
                selected = false,
                onClick = { navController.navigate(screen.route) }
            )
        }
    }
}