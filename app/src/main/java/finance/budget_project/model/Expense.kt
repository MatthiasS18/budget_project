package finance.budget_project.model

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.PrimaryKey


data class Expense(
    val name: String,
    val category: ExpenseCategory,
    val amount: Float
)


/*
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val categoryId: Int,
    val amount: Float
)

*/
