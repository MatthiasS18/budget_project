package finance.budget_project.ui.expenses

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import finance.budget_project.model.Expense
import finance.budget_project.model.ExpenseCategory

class ExpenseViewModel : ViewModel() {

    var budget by mutableStateOf(5421.34)
        private set

    // Liste des dépenses
    private val _expenses = mutableStateListOf<Expense>()
    val expenses: List<Expense> get() = _expenses

    // Total calculé dynamiquement
    val expenseTotal: Double
        get() = _expenses.sumOf { it.amount.toDouble() }

    // Reste disponible automatiquement calculé
    val restAvailable: Double
        get() = budget - expenseTotal

    // Progression dynamique (0.0 à 1.0)
    val progress: Float
        get() = (expenseTotal / budget).toFloat().coerceIn(0f, 1f)

    fun addExpense(expense: Expense) {
        _expenses.add(expense)
        name = ""
        amount = ""
        Log.d("ExpenseVM", "Expenses: $_expenses")
    }

    fun updateBudget(newBudget: Double) {
        budget = newBudget
    }

    var showDialog by  mutableStateOf(false)
    var name by   mutableStateOf("")
    var amount by   mutableStateOf("")
    var selectedIcon by   mutableStateOf(Icons.Default.Fastfood)
    var selectedColor by   mutableStateOf(Color(0xFFFFA726))


    var selectedCategory by mutableStateOf(ExpenseCategory.FOOD)}
