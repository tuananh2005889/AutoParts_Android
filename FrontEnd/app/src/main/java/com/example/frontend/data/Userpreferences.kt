package com.example.frontend.data

import android.content.Context
import com.example.frontend.data.model.LoginData
import com.example.frontend.data.model.UserData

fun saveUserData(context: Context, user: LoginData) {
    val sharedPref = context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString("userName", user.userName)
        putString("password", user.password)
        apply()
    }
}

fun getUserData(context: Context): LoginData? {
    val sharedPref = context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
    val userName = sharedPref.getString("userName", null)
    val password = sharedPref.getString("password", null)
    return if (userName != null  && password != null) {
        LoginData(userName, password)
    } else {
        null
    }
}
