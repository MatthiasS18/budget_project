package finance.budget_project.ui.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import finance.budget_project.ui.auth.AuthViewModel

@Composable
fun LoginScreen(
    navController : NavHostController,
    toScreen : String,
    loginViewModel: LoginViewModel = viewModel()
){
    var context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome Back!",
            color = Color(red = 120, green = 112, blue = 163),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )

        Text("Let's sign you in to your account")
        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = loginViewModel.mail,
            onValueChange = { newValue ->
                val sanitizedValue = newValue.replace("\n", "")
                loginViewModel.mail = sanitizedValue
            },
            label = {
                Text(
                    text = "Email",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Serif
                )
            },
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = loginViewModel.textValuePassword,
            onValueChange = { newValue ->
                val sanitizedValue = newValue.replace("\n", "")
                loginViewModel.textValuePassword = sanitizedValue
            },
            label = {
                Text(
                    text = "Password",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Serif
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                AuthViewModel.login(
                    loginViewModel.mail,
                    loginViewModel.textValuePassword
                ) { success ->
                    if (success) {
                        Log.d("super", "super login")
                        navController.navigate(toScreen)
                    } else {
                        Log.d("failed", "super login failed")
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(87,26,162),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Sign In",
                fontFamily = FontFamily.Serif,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                )
            )
        }
    }
}