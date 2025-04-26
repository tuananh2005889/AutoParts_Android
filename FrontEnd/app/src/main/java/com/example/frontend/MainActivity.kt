package com.example.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.frontend.ui.navigation.AppNavHost
import com.example.frontend.ui.theme.FrontEndTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.content.ContextCompat

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            window.statusBarColor = ContextCompat.getColor(this, R.color.dark_background)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.dark_background)
            FrontEndTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }
}


