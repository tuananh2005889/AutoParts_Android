package com.example.frontend.ui.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_settings")

object AuthPreferencesKeys {
    val isLoggedIn = booleanPreferencesKey("is_logged_in")
    val userName = stringPreferencesKey("user_name")
}

@Singleton
class AuthManager @Inject constructor(@ApplicationContext private val context: Context) {
    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[AuthPreferencesKeys.isLoggedIn] ?: false
        }
    val userNameFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[AuthPreferencesKeys.userName]
        }

    suspend fun saveLoginStatus(isLoggedIn: Boolean, userName: String?) {
        context.dataStore.edit { preferences ->
            preferences[AuthPreferencesKeys.isLoggedIn] = isLoggedIn
            if (userName != null) {
                preferences[AuthPreferencesKeys.userName] = userName
            }
        }
    }

suspend fun clearLoginStatus() {
    context.dataStore.edit { preferences ->
        preferences.remove(AuthPreferencesKeys.isLoggedIn)
        preferences.remove(AuthPreferencesKeys.userName)
    }
}

suspend fun isLoggedInOnce(): Boolean {
    return context.dataStore.data.first()[AuthPreferencesKeys.isLoggedIn] ?: false
}

    suspend fun getUserNameOnce(): String? {
        return context.dataStore.data.first()[AuthPreferencesKeys.userName]
    }
}