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
import androidx.navigation.toRoute
import com.ynab.ui.theme.YNABTheme
import com.ynab.ui.login.Login
import com.ynab.ui.register.Register
import com.ynab.ui.splash.Splash
import com.ynab.ui.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

const val TAG_PREFIX = "YNAB_"
private const val TAG = "${TAG_PREFIX}MainActivity"

@AndroidEntryPoint
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
                onRegisterClick = { navController.navigate(route = Register) },
                onLoginSuccess = { navController.navigate(route = Splash(isNewUser = false)) }
            )
        }
        composable<Register> {
            Register(
                modifier = modifier,
                onLoginClick = { navController.navigate(route = Login) },
                onRegisterSuccess = { navController.navigate(route = Splash(isNewUser = true)) }
            )
        }
        composable<Splash> { backStackEntry ->
            Splash(
                isNewUser = (backStackEntry.toRoute() as Splash).isNewUser,
                modifier = modifier,
                onStartupComplete = { Log.d(TAG, "Startup Complete") }
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