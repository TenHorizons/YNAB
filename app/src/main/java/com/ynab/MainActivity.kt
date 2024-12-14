package com.ynab

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ynab.ui.theme.YNABTheme
import com.ynab.ui.login.Login

private const val TAG = "MainActivity"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YNABTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Main(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Main(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Login) {
        composable<Login> {
            Login(
                modifier = modifier,
                onRegisterClick = { Log.d(TAG, "Nav to register success")/*navController.navigate(route = Register)*/ },
                onLoginSuccess = { Log.d(TAG, "Login Success") }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    YNABTheme {
        Main()
    }
}