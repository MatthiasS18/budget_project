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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import finance.budget_project.model.dataBaseModel.Repository
import finance.budget_project.ui.auth.AuthViewModel
import finance.budget_project.ui.login.LoginScreen
import finance.budget_project.ui.signUp.SignUpScreen

class MainActivity : ComponentActivity() {

    enum class CurrentScreen {
        SignUp, LogIn, Home, Expense
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Repository.initDataBase(this)
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
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        AuthViewModel.initAuth(context)
    }

    var selectedScreen by rememberSaveable {
        mutableIntStateOf(
            if (AuthViewModel.isLoggedIn) MainActivity.CurrentScreen.Home.ordinal
            else MainActivity.CurrentScreen.LogIn.ordinal
        )
    }


    LaunchedEffect(AuthViewModel.isLoggedIn) {
        if (AuthViewModel.isLoggedIn) {
            navController.navigate(MainActivity.CurrentScreen.Home.name) {
                popUpTo(0)
                launchSingleTop = true
            }
        } else {
            // Déconnecté → montrer Login
            navController.navigate(MainActivity.CurrentScreen.LogIn.name) {
                popUpTo(0)
                launchSingleTop = true
            }
            selectedScreen = MainActivity.CurrentScreen.LogIn.ordinal
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                // Liste filtrée selon login
                val visibleScreens = if (AuthViewModel.isLoggedIn) {
                    listOf(MainActivity.CurrentScreen.Home, MainActivity.CurrentScreen.Expense)
                } else {
                    listOf(MainActivity.CurrentScreen.SignUp, MainActivity.CurrentScreen.LogIn)
                }

                visibleScreens.forEachIndexed { index, screen ->
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
                selectedScreen = MainActivity.CurrentScreen.Home.ordinal

            }
        }
    ) { paddingValues ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(paddingValues),
            authViewModel = AuthViewModel
        )
    }


}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = MainActivity.CurrentScreen.LogIn.name,
        modifier = modifier
    ) {
        composable(MainActivity.CurrentScreen.Home.name) {
            HomePageScreen(
            )
        }
        composable(MainActivity.CurrentScreen.Expense.name) {
            ExpenseScreen(
            )
        }
        composable(MainActivity.CurrentScreen.SignUp.name) {
            val toScreen = MainActivity.CurrentScreen.LogIn.name
            SignUpScreen(
                navController = navController,
                toScreen = toScreen,
            )
        }
        composable(MainActivity.CurrentScreen.LogIn.name) {
            val toScreen = MainActivity.CurrentScreen.Home.name
            LoginScreen(
                navController = navController,
                toScreen = toScreen,
            )
        }
    }
}

