package com.example.frontend.data

import android.content.Context
import com.example.frontend.data.model.LoginData
import com.example.frontend.data.model.UserData

object UserPreferences {
    private const val PREFS_NAME = "myAppPrefs"
    private const val KEY_USER_NAME = "userName"


    fun saveUserData(context: Context, user: LoginData) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(KEY_USER_NAME, user.userName)
            apply()
        }
    }

    fun getUserData(context: Context): LoginData? {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val userName = sharedPref.getString(KEY_USER_NAME, null)
        return if (userName != null) {
            LoginData(
                userName = userName,
                password = "",
            )
        } else {
            null
        }
    }

    fun clearUserData(context: Context) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear() // xóa toàn bộ
            apply()
        }
    }
}

