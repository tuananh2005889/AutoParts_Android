package com.example.frontend.ui.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.frontend.data.model.UserData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_settings")

object AuthPreferencesKeys {
    val userId = longPreferencesKey("user_id")
    val isLoggedIn = booleanPreferencesKey("is_logged_in")
    val userName = stringPreferencesKey("user_name")
    val fullName = stringPreferencesKey("full_name")
    val gmail = stringPreferencesKey("gmail")
    val phone = stringPreferencesKey("phone")
    val password = stringPreferencesKey("password")
    val avatarUrl = stringPreferencesKey("avatar_url") // <-- thêm dòng này
    val cartId = longPreferencesKey("cart_id")
    val authToken = stringPreferencesKey("auth_token")
    val currentQRCode = stringPreferencesKey("current_qr_code")
    val currentPendingOrderCode = longPreferencesKey("current_pending_order_code")
}

@Singleton
class AuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { it[AuthPreferencesKeys.isLoggedIn] ?: false }

    val userNameFlow: Flow<String?> = context.dataStore.data
        .map { it[AuthPreferencesKeys.userName] }

    val cartIdFlow: Flow<Long?> = context.dataStore.data
        .map { it[AuthPreferencesKeys.cartId] }

    val authTokenFlow: Flow<String?> = context.dataStore.data
        .map { it[AuthPreferencesKeys.authToken] }

    val currentQRCodeFlow: Flow<String?> = context.dataStore.data
        .map { it[AuthPreferencesKeys.currentQRCode] }

    // Khi login thành công sẽ lưu luôn trạng thái đăng nhập
    suspend fun saveLoginStatus(
        isLoggedIn: Boolean,
        userName: String?,
        cartId: Long? = null,
        authToken: String? = null
    ) {
        context.dataStore.edit { prefs ->
            prefs[AuthPreferencesKeys.isLoggedIn] = isLoggedIn

            userName?.let { prefs[AuthPreferencesKeys.userName] = it }
            cartId?.let { prefs[AuthPreferencesKeys.cartId] = it }
            authToken?.let { prefs[AuthPreferencesKeys.authToken] = it }
        }
    }

    suspend fun clearLoginStatus() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }

    suspend fun isLoggedInOnce(): Boolean =
        context.dataStore.data.first()[AuthPreferencesKeys.isLoggedIn] ?: false

    suspend fun getUserNameOnce(): String? =
        context.dataStore.data.first()[AuthPreferencesKeys.userName]

    suspend fun getCartIdOnce(): Long? =
        context.dataStore.data.first()[AuthPreferencesKeys.cartId]

    suspend fun getAuthTokenOnce(): String? =
        context.dataStore.data.first()[AuthPreferencesKeys.authToken]

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[AuthPreferencesKeys.authToken] = token
        }
    }

    suspend fun clearAuthToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(AuthPreferencesKeys.authToken)
        }
    }

    val userDataFlow: Flow<UserData?> = context.dataStore.data
        .map { preferences ->
            val userId = preferences[AuthPreferencesKeys.userId]
            val userName = preferences[AuthPreferencesKeys.userName]
            val fullName = preferences[AuthPreferencesKeys.fullName]
            val gmail = preferences[AuthPreferencesKeys.gmail]
            val phone = preferences[AuthPreferencesKeys.phone]
            val password = preferences[AuthPreferencesKeys.password]
            val avatarUrl = preferences[AuthPreferencesKeys.avatarUrl]

            if (userName != null && fullName != null && gmail != null && phone != null && password != null) {
                UserData(
                    userId = userId,
                    userName = userName,
                    fullName = fullName,
                    gmail = gmail,
                    phone = phone,
                    password = password,
                    avatarUrl = avatarUrl  // <- thêm avatarUrl ở đây
                )
            } else {
                null
            }
        }

    suspend fun saveUserData(userData: UserData) {
        context.dataStore.edit { prefs ->
            userData.userId?.let { prefs[AuthPreferencesKeys.userId] = it }
            prefs[AuthPreferencesKeys.userName] = userData.userName
            prefs[AuthPreferencesKeys.fullName] = userData.fullName
            prefs[AuthPreferencesKeys.gmail]    = userData.gmail
            prefs[AuthPreferencesKeys.phone]    = userData.phone
            prefs[AuthPreferencesKeys.password] = userData.password.toString()
            userData.avatarUrl?.let { prefs[AuthPreferencesKeys.avatarUrl] = it }
        }
    }
    suspend fun getUserData(): UserData? {
        val preferences = context.dataStore.data.first() // Lấy dữ liệu lần đầu tiên
        val userId = preferences[AuthPreferencesKeys.userId]
        val userName = preferences[AuthPreferencesKeys.userName]
        val fullName = preferences[AuthPreferencesKeys.fullName]
        val gmail = preferences[AuthPreferencesKeys.gmail]
        val phone = preferences[AuthPreferencesKeys.phone]
        val password  = preferences[AuthPreferencesKeys.password]
        val avatarUrl = preferences[AuthPreferencesKeys.avatarUrl]

        // Kiểm tra các trường bắt buộc, nếu đầy đủ thì trả về UserData
        return if (userName != null && fullName != null && gmail != null && phone != null) {
            UserData(
                userId = userId,
                userName = userName,
                fullName = fullName,
                gmail = gmail,
                phone = phone,
                password = password,
                avatarUrl = avatarUrl
            )
        } else {
            null
        }
    }

    suspend fun getCurrentQRCodeOnce(): String? =
        context.dataStore.data.first()[AuthPreferencesKeys.currentQRCode]

    suspend fun saveCurrentQRCode(qrCode: String) {
        context.dataStore.edit { prefs ->
            prefs[AuthPreferencesKeys.currentQRCode] = qrCode
        }
    }

    suspend fun clearCurrentQRCode() {
        context.dataStore.edit { prefs ->
            prefs.remove(AuthPreferencesKeys.currentQRCode)
        }
    }

    suspend fun getCurrentPendingOrderCodeOnce(): Long? =
        context.dataStore.data.first()[AuthPreferencesKeys.currentPendingOrderCode]

    suspend fun saveCurrentPendingOrderCode(pendingOrderCode: Long) {
        context.dataStore.edit { prefs ->
            prefs[AuthPreferencesKeys.currentPendingOrderCode] = pendingOrderCode
        }
    }
    suspend fun clearCurrentPendingOrderCode() {
        context.dataStore.edit { prefs ->
            prefs.remove(AuthPreferencesKeys.currentPendingOrderCode)
        }
    }
}
