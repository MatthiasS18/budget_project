package finance.budget_project.model

import androidx.compose.ui.graphics.vector.ImageVector

data class Expense(
    val name: String,
    val amount: Float,
    val icon: ImageVector,
    val color: androidx.compose.ui.graphics.Color
)
