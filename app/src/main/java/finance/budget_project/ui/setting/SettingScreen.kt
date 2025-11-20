package finance.budget_project.ui.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import finance.budget_project.MainActivity
import finance.budget_project.ui.auth.AuthViewModel

@Composable
fun SettingScreen(settingViewModel: SettingViewModel = viewModel(),
                  navController: NavController,
                  toScreen: String
) {

    val context = LocalContext.current

    Column {
        TopRowWithLogout(settingViewModel = settingViewModel,
            onLogoutConfirmed = {
                AuthViewModel.logout(context)
                navController.navigate(toScreen) {
                    popUpTo(MainActivity.CurrentScreen.LogIn.name) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        )

        // ... le reste de ton écran
    }

}

@Composable
fun TopRowWithLogout(
    settingViewModel: SettingViewModel = viewModel(),
    onLogoutConfirmed: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End

    ) {
        // Bouton logout
        TextButton(
            onClick = { settingViewModel.showDialog = true }
        ) {
            Icon(
                imageVector = Icons.Filled.Logout,
                contentDescription = "Logout"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout")
        }

    }

    if (settingViewModel.showDialog) {
        AlertDialog(
            onDismissRequest = { settingViewModel.showDialog = false },
            title = { Text("Logout") },
            text = { Text("Do you want really to logout ?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        settingViewModel.showDialog = false
                        onLogoutConfirmed()
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { settingViewModel.showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}



