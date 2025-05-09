package com.example.frontend
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.frontend.ui.navigation.AppNavHost
import com.example.frontend.ui.theme.FrontEndTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.content.ContextCompat
import android.util.Log
import android.widget.Toast


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Kiểm tra Intent có chứa URL không
        val intent: Intent = intent
        val data: Uri? = intent.data

        // Nếu có URL được gửi tới, xử lý theo URL
        data?.let { uri ->
            if (uri.host == "payment-result") {
                handlePaymentSuccess()
            } else if (uri.host == "payment-cancel") {
                handlePaymentCancel()
            }
        }

        setContent {
            window.statusBarColor = ContextCompat.getColor(this, R.color.dark_background)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.dark_background)
            FrontEndTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }

    private fun handlePaymentSuccess() {
        // Thực hiện hành động khi thanh toán thành công
        Log.d("MainActivity", "Thanh toán thành công")
        Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show()
        // Bạn có thể điều hướng hoặc làm gì đó sau khi thanh toán thành công
    }

    private fun handlePaymentCancel() {
        // Thực hiện hành động khi thanh toán bị hủy
        Log.d("MainActivity", "Thanh toán bị hủy")
        Toast.makeText(this, "Thanh toán bị hủy!", Toast.LENGTH_SHORT).show()
        // Bạn có thể điều hướng hoặc làm gì đó khi thanh toán bị hủy
    }
}


