package com.example.frontend.ui.common

import java.text.NumberFormat
import java.util.Locale

    fun Double.formatAsCurrency(): String {
        val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN")).apply {
            maximumFractionDigits = 0
            isGroupingUsed = true
        }
        val formattedNumber = formatter.format(this)
        return "$formattedNumber VNĐ"
    }