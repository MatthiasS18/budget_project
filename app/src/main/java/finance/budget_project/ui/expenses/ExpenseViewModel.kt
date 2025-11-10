package finance.budget_project.ui.expenses

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ExpenseViewModel : ViewModel() {

    var budget by mutableStateOf(5421.34)
}