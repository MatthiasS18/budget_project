package finance.budget_project.ui.homePage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import finance.budget_project.model.Expense
import finance.budget_project.model.ExpenseCategory
import finance.budget_project.model.dataBaseModel.Repository
import kotlinx.coroutines.launch

class HomePageViewModel : ViewModel() {


    val expenses = Repository.expenses
    val budget = Repository.budget
    val expenseTotal get() = Repository.expenseTotal

    var newBudgetText by mutableStateOf("")


    init {
        viewModelScope.launch {
            val entities = Repository.getAllExpensesFromDatabase()

            val expenseList = entities.map { entity ->
                Expense(
                    name = entity.name,
                    amount = entity.amount,
                    category = ExpenseCategory.fromId(entity.categoryId)
                        ?: ExpenseCategory.FOOD
                )
            }

            Repository.setExpenses(expenseList)
            Repository.loadBudget()
            newBudgetText = Repository.budget.value.toString()
        }
    }


}