package finance.budget_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import finance.budget_project.ui.expenses.ExpenseScreen
import finance.budget_project.ui.homePage.HomePageScreen
import finance.budget_project.ui.theme.Budget_projectTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import finance.budget_project.ui.login.LoginScreen
import finance.budget_project.ui.signUp.SignUpScreen

class MainActivity : ComponentActivity() {

    enum class CurrentScreen {
        SignUp, LogIn, Home, Expense
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Budget_projectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    MainScreen()
                }

            }

            }
        }
    }



@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var selectedScreen by rememberSaveable { mutableIntStateOf(MainActivity.CurrentScreen.Home.ordinal) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                MainActivity.CurrentScreen.entries.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        selected = selectedScreen == index,
                        onClick = {
                            selectedScreen = index
                            navController.navigate(screen.name) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = when (screen) {
                                    MainActivity.CurrentScreen.Home -> Icons.Default.Home
                                    MainActivity.CurrentScreen.Expense -> Icons.Default.AttachMoney
                                    MainActivity.CurrentScreen.SignUp -> Icons.Default.PersonAdd
                                    MainActivity.CurrentScreen.LogIn -> Icons.AutoMirrored.Filled.Login
                                },
                                contentDescription = screen.name
                            )
                        },
                        label = { Text(screen.name) }
                    )
                }
            }
        }
    ) { paddingValues ->
        AppNavHost(navController, Modifier.padding(paddingValues))
    }
}

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = MainActivity.CurrentScreen.Home.name,
        modifier = modifier
    ) {
        composable(MainActivity.CurrentScreen.Home.name) {
            HomePageScreen()
        }
        composable(MainActivity.CurrentScreen.Expense.name) {
            ExpenseScreen()
        }
        composable(MainActivity.CurrentScreen.SignUp.name) {
             SignUpScreen()
        }
        composable(MainActivity.CurrentScreen.LogIn.name) {
             LoginScreen()
        }
    }
}

