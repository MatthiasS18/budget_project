package finance.budget_project.ui.signUp

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel = viewModel()
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Your Account",
            color = Color(red = 120, green = 112, blue = 163),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )

        Text("Join us and start managing your budget")
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = signUpViewModel.fullName,
            onValueChange = { newValue ->
                val sanitizedValue = newValue.replace("\n", "")
                signUpViewModel.fullName = sanitizedValue
            },
            label = {
                Text(
                    text = "Full Name",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Serif
                )
            },
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = signUpViewModel.mail,
            onValueChange = { newValue ->
                val sanitizedValue = newValue.replace("\n", "")
                signUpViewModel.mail = sanitizedValue
            },
            label = {
                Text(
                    text = "Email",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Serif
                )
            },
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = signUpViewModel.textValuePassword,
            onValueChange = { newValue ->
                val sanitizedValue = newValue.replace("\n", "")
                signUpViewModel.textValuePassword = sanitizedValue
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

        OutlinedTextField(
            value = signUpViewModel.textValuePasswordConfirm,
            onValueChange = { newValue ->
                val sanitizedValue = newValue.replace("\n", "")
                signUpViewModel.textValuePasswordConfirm = sanitizedValue
            },
            label = {
                Text(
                    text = "Confirm Password",
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
                // navController.navigate(currentScreen[0].name)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(87,26,162),
                contentColor = Color(255,255,255),

            ),modifier = Modifier.fillMaxWidth()
) {
            Text(
                text = "Sign Up",
                fontFamily = FontFamily.Serif,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                )
            )
        }
    }



}