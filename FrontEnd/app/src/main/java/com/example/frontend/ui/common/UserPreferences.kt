//import android.content.Context
//import androidx.datastore.core.*
//import androidx.datastore.preferences.core.*
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.core.stringPreferencesKey
//import androidx.datastore.preferences.preferencesDataStore
//import dagger.hilt.android.qualifiers.ApplicationContext
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//import javax.inject.Inject
//import javax.inject.Singleton
//
//
//
// val Context.store2: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")
//
// val USER_NAME_KEY = stringPreferencesKey("user_name")
//
//@Singleton
//class UserPreferences @Inject constructor(
//    @ApplicationContext private val context: Context
//) {
//
//    suspend fun saveUserName(userName: String) {
//        context.store2.edit { preferences ->
//            preferences[USER_NAME_KEY] = userName
//        }
//    }
//
//    val userNameFlow: Flow<String?> = context.store2.data
//        .map { preferences ->
//            preferences[USER_NAME_KEY]
//        }
//
//    suspend fun clearUserName() {
//        context.store2.edit { preferences ->
//            preferences.remove(USER_NAME_KEY)
//        }
//    }
//}