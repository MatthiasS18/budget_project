package finance.budget_project.ui.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import finance.budget_project.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

object AuthViewModel : ViewModel() {
    var jwtToken by mutableStateOf<String?>(null)
        private set

    var authError by mutableStateOf<String?>(null)
        private set





    fun signup(username: String, email: String, password: String) {
        ApiClient.signup(username, email, password) { success, message ->
            if (success) {
                authError = null
            } else {
                authError = message
            }
        }
    }


    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        ApiClient.login(email, password) { success, message ->
            // Ici on est dans un thread background
            viewModelScope.launch(Dispatchers.Main) {
                if (success) {
                    val token = JSONObject(message).getString("token")
                    jwtToken = token
                    authError = null
                    Log.d("AuthViewModel", "Login successful. Token: $token")

                    // ✅ Appel du callback pour indiquer le succès
                    onResult(true)
                } else {
                    authError = message
                    Log.d("AuthViewModel", "Login failed. Error: $message")
                    onResult(false)
                }
            }
        }
    }


}
