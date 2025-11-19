package finance.budget_project.ui.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


val Context.authDataStore by preferencesDataStore(name = "auth_prefs")

object AuthDataStore {

    private val KEY_TOKEN = stringPreferencesKey("jwt_token")
    private val FULL_NAME = stringPreferencesKey("full_Name")


    fun getTokenFlow(context: Context): Flow<String?> {
        return context.authDataStore.data.map { prefs ->
            prefs[KEY_TOKEN]
        }
    }



    fun getFullNameFlow(context: Context): Flow<String?> {
        return context.authDataStore.data.map { prefs ->
            prefs[FULL_NAME]
        }
    }


    suspend fun saveToken(context: Context, token: String) {
        context.authDataStore.edit { prefs ->
            prefs[KEY_TOKEN] = token
        }
    }


    suspend fun saveFullName(context: Context, name: String) {
        context.authDataStore.edit { prefs ->
            prefs[FULL_NAME] = name
        }
    }


    suspend fun clearToken(context: Context) {
        context.authDataStore.edit { prefs ->
            prefs.remove(KEY_TOKEN)
        }
    }
}