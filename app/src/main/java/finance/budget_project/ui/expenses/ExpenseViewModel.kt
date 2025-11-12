package finance.budget_project.ui.expenses

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import finance.budget_project.model.ExpenseCategory
import finance.budget_project.model.dataBaseModel.Repository
import finance.budget_project.model.dataBaseModel.Repository.expenseTotal

class ExpenseViewModel : ViewModel() {

    var showEditDialog by mutableStateOf(false)

    var showDialog by  mutableStateOf(false)

    var name by   mutableStateOf("")
    var amount by   mutableStateOf("")
    var selectedIcon by   mutableStateOf(Icons.Default.Fastfood)
    var selectedColor by   mutableStateOf(Color(0xFFFFA726))

    var selectedCategory by mutableStateOf(ExpenseCategory.FOOD)

    val progress: Float
        get() = (expenseTotal / Repository.budget.value).toFloat().coerceIn(0f, 1f)

}




