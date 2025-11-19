package finance.budget_project.ui.auth

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import finance.budget_project.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject

object AuthViewModel : ViewModel() {

    var jwtToken: String? = null
        private set

    var fullName by mutableStateOf("")
        private set

    var authError by mutableStateOf<String?>(null)
        private set

    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: Boolean get() = _isLoggedIn.value


    /**
     * Appelé au démarrage de l’app (depuis MainScreen)
     */
    fun initAuth(context: Context) {
        viewModelScope.launch {
            jwtToken = AuthDataStore.getTokenFlow(context).first()
            fullName = AuthDataStore.getFullNameFlow(context).first() ?: ""
            _isLoggedIn.value = jwtToken != null
        }
    }


    fun signup(context: Context, fullName: String, email: String, password: String) {
        ApiClient.signup(fullName, email, password) { success, message ->
            if (success) {
                authError = null
                viewModelScope.launch {
                    AuthDataStore.saveFullName(context, fullName)
                }
            } else {
                authError = message
            }
        }
    }


    fun login(
        context: Context,
        email: String,
        password: String,
        onResult: (Boolean) -> Unit
    ) {
        ApiClient.login(email, password) { success, message ->
            viewModelScope.launch(Dispatchers.Main) {
                if (success) {
                    val token = JSONObject(message).getString("token")
                    jwtToken = token
                    _isLoggedIn.value = true

                    // Sauvegarde dans DataStore
                    viewModelScope.launch {
                        AuthDataStore.saveToken(context, token)
                    }





                    onResult(true)
                } else {
                    _isLoggedIn.value = false
                    onResult(false)
                }
            }
        }
    }


    fun logout(context: Context) {
        viewModelScope.launch {
            AuthDataStore.clearToken(context)
            jwtToken = null
            _isLoggedIn.value = false
        }
    }
}
