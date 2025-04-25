package com.example.frontend.ui.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_settings")
object AuthPreferencesKeys {
    val isLoggedIn  = booleanPreferencesKey("is_logged_in")
    val userName    = stringPreferencesKey("user_name")
    val cartId      = longPreferencesKey("cart_id")
    val authToken   = stringPreferencesKey("auth_token")
}

@Singleton
class AuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // Flows hiện có
    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { it[AuthPreferencesKeys.isLoggedIn] ?: false }

    val userNameFlow: Flow<String?> = context.dataStore.data
        .map { it[AuthPreferencesKeys.userName] }

    val cartIdFlow: Flow<Long?> = context.dataStore.data
        .map { it[AuthPreferencesKeys.cartId] }

    // 2) Flow cho JWT token
    val authTokenFlow: Flow<String?> = context.dataStore.data
        .map { it[AuthPreferencesKeys.authToken] }

    // 3) Khi login thành công (Google hay username/password) sẽ gọi hàm này
    //    lưu luôn isLoggedIn, userName, cartId (nếu có) và authToken (nếu truyền vào)
    suspend fun saveLoginStatus(
        isLoggedIn: Boolean,
        userName: String?,
        cartId: Long? = null,
        authToken: String? = null      // ← nhận thêm token
    ) {
        context.dataStore.edit { prefs ->
            prefs[AuthPreferencesKeys.isLoggedIn] = isLoggedIn
            userName?.let { prefs[AuthPreferencesKeys.userName] = it }
            cartId  ?.let { prefs[AuthPreferencesKeys.cartId]   = it }
            authToken?.let { prefs[AuthPreferencesKeys.authToken] = it }
        }
    }

    // 4) Xoá hết khi logout
    suspend fun clearLoginStatus() {
        context.dataStore.edit { prefs ->
            prefs.clear()  // xoá toàn bộ, bao gồm authToken
        }
    }

    // 5) Các hàm “once” vẫn hoạt động bình thường
    suspend fun isLoggedInOnce(): Boolean =
        context.dataStore.data.first()[AuthPreferencesKeys.isLoggedIn] ?: false

    suspend fun getUserNameOnce(): String? =
        context.dataStore.data.first()[AuthPreferencesKeys.userName]

    suspend fun getCartIdOnce(): Long? =
        context.dataStore.data.first()[AuthPreferencesKeys.cartId]

    // Mới: lấy JWT lần đầu (nếu cần)
    suspend fun getAuthTokenOnce(): String? =
        context.dataStore.data.first()[AuthPreferencesKeys.authToken]

    // Mới: chỉ lưu token riêng
    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[AuthPreferencesKeys.authToken] = token
        }
    }

    // Mới: xoá riêng token
    suspend fun clearAuthToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(AuthPreferencesKeys.authToken)
        }
    }
}