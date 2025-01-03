package com.ynab.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ynab.ui.shared.PasswordField
import com.ynab.ui.shared.UsernameField

@Composable
fun Login(
    modifier: Modifier = Modifier,
    vm: LoginViewModel = hiltViewModel(),
    onRegisterClick: () -> Unit = {},
    onLoginSuccess: () -> Unit = {}
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val kbController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val textFieldModifier = Modifier.fillMaxWidth().padding(16.dp,4.dp)

        UsernameField(uiState.username, vm::onUsernameChange, textFieldModifier)
        PasswordField(uiState.password, vm::onPasswordChange, textFieldModifier)

        Button(
            onClick = {
                kbController?.hide()
                vm.onLoginClick(onLoginSuccess = onLoginSuccess)
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp,8.dp)
        ) {
            Text(text = "Sign In", fontSize = 16.sp)
        }

        if (uiState.isLoginInProgress)
            CircularProgressIndicator()
        else
            TextButton(
                onClick = { onRegisterClick() },
                modifier = Modifier.fillMaxWidth().padding(16.dp,8.dp,16.dp,0.dp)
            ) {
                Text("New User? Click to Sign Up")
            }
        if (uiState.isLoginError) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(uiState.errorMessage)
            }
            vm.hideAfterDelay(duration = 8000L)
        }
    }
}