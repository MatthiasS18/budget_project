package finance.budget_project.ui.signUp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel(){

    var fullName by mutableStateOf("")
    var mail by mutableStateOf("")
    var textValuePassword by mutableStateOf("")
    var textValuePasswordConfirm by mutableStateOf("")


}