package finance.budget_project.model.dataBaseModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import finance.budget_project.model.Expense


object Repository {

    // --- Liste des dépenses partagée ---
    private val _expenses = mutableStateListOf<Expense>()
    val expenses: List<Expense> get() = _expenses

    // --- Budget et calculs ---
    var budget = mutableStateOf(0.0)
    val expenseTotal get() = _expenses.sumOf { it.amount.toDouble() }
    val restAvailable get() = budget.value - expenseTotal

    // --- Fonctions utilitaires ---
    fun addExpense(expense: Expense): Boolean {
        return if (expenseTotal + expense.amount <= budget.value) {
            _expenses.add(expense)
            true
        } else {
            false // dépassement du budget
        }
    }

    fun updateBudget(newBudget: Double) {
        budget.value = newBudget
    }

    fun clearExpenses() {
        _expenses.clear()
    }
}
