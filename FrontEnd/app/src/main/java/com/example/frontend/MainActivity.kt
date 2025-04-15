package com.example.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.frontend.ui.navigation.AppNavHost
import com.example.frontend.ui.theme.FrontEndTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontEndTheme {
                val navController = rememberNavController()
                val isLoggedIn = remember { mutableStateOf(false)}
                AppNavHost(navController, isLoggedIn)
            }
        }
    }
}


