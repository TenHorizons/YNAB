package com.ynab.ui.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun Splash(
    modifier: Modifier = Modifier,
    isNewUser: Boolean,
    onStartupComplete: () -> Unit,
    vm: SplashViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val uiState by vm.uiState.collectAsStateWithLifecycle()
        if (uiState.showError) {
            Text(text = uiState.errorMessage)

            Button(
                modifier = Modifier.fillMaxWidth().padding(16.dp, 8.dp),
                onClick = { vm.onAppStart(isNewUser, onStartupComplete) }
            ){
                Text(text = "Try again", fontSize = 16.sp)
            }
        } else {
            CircularProgressIndicator()
        }
    }

    LaunchedEffect(true) {
        vm.onAppStart(isNewUser, onStartupComplete)
    }
}