package finance.budget_project.model

import androidx.compose.ui.graphics.vector.ImageVector


data class Expense(
    val name: String,
    val category: ExpenseCategory,
    val amount: Float
)
