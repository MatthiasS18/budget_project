package finance.budget_project.ui.expenses

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import finance.budget_project.database.expenseDatabase.ExpenseEntity
import finance.budget_project.model.Expense
import finance.budget_project.model.ExpenseCategory
import finance.budget_project.model.dataBaseModel.Repository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ExpenseViewModel : ViewModel() {


    val expenses = Repository.expenses

    var dateOfLastExpense = expenses.lastOrNull()?.date
    val budget = Repository.budget
    val expenseTotal get() = Repository.expenseTotal
    val restAvailable get() = Repository.restAvailable


    var showEditDialog by mutableStateOf(false)

    var showDialog by  mutableStateOf(false)

    var name by   mutableStateOf("")
    var amount by   mutableStateOf("")
    var selectedIcon by   mutableStateOf(Icons.Default.Fastfood)
    var selectedColor by   mutableStateOf(Color(0xFFFFA726))

    var selectedCategory by mutableStateOf(ExpenseCategory.FOOD)

    var newBudgetText by mutableStateOf("")

    val progress: Float
        get() = (expenseTotal / Repository.budget.doubleValue).toFloat().coerceIn(0f, 1f)

    var isSameDayExpense by mutableStateOf("")



    init {
        viewModelScope.launch {
            val entities = Repository.getAllExpensesFromDatabase()

            val expenseList = entities.map { entity ->
                Expense(
                    name = entity.name,
                    amount = entity.amount,
                    category = ExpenseCategory.fromId(entity.categoryId)
                        ?: ExpenseCategory.FOOD,
                    date = entity.date
                )
            }

            Repository.setExpenses(expenseList)
            Repository.loadBudget()
            newBudgetText = Repository.budget.value.toString()
        }
    }




    suspend fun addExpense(expense: Expense): Boolean {
        val canAdd = Repository.addExpense(expense)
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        if (canAdd) {
            val entity = ExpenseEntity(
                name = expense.name,
                categoryId = expense.category.id,
                amount = expense.amount,
                date = today
            )
            Repository.insertExpenseInDatabase(entity)
            isSameDayExpense = expense.date
            return true
        } else {
            return false
        }
    }



    suspend fun updateBudget(amount: Double) {
        Repository.saveBudgetToDatabase(amount)
    }

    fun clear() {
        Repository.clearExpenses()
    }
}




