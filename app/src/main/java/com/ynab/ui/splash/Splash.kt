package com.ynab.ui.splash

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ynab.TAG_PREFIX

private const val TAG = "${TAG_PREFIX}SplashScreen"

@Composable
fun Splash(
    isNewUser: Boolean,
    onStartupComplete: () -> Unit,
    modifier: Modifier = Modifier,
    vm: SplashViewModel = hiltViewModel()
) {
    val start = System.currentTimeMillis()
    Log.d(TAG,"splash screen start: $start")
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val uiState by vm.uiState
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

    val end = System.currentTimeMillis()
    Log.d(TAG,"splash screen end: $end")
    Log.d(TAG,"splash screen time: ${end-start}")
}