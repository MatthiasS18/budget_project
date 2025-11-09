package finance.budget_project.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel(){

    var mail by mutableStateOf("")
    var textValuePassword by mutableStateOf("")
}