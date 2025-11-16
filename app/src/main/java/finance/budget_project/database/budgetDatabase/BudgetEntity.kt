package finance.budget_project.database.budgetDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget")
data class BudgetEntity(
    @PrimaryKey val id: Int = 0,
    val amount: Double
)