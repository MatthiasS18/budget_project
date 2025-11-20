package finance.budget_project.model


data class Expense(
    val name: String,
    val category: ExpenseCategory,
    val amount: Float,
    val date: String
)

