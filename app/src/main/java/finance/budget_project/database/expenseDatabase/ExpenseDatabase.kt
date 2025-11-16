package finance.budget_project.database.expenseDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import finance.budget_project.database.budgetDatabase.BudgetEntity


@Database(
    entities = [
        ExpenseEntity::class,
        BudgetEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun theDAO(): ExpenseDAO

    companion object {
        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        fun getInstance(context: Context): ExpenseDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_db"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}