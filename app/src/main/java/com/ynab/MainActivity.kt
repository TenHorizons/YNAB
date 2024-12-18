package com.ynab

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ynab.ui.accounts.Accounts
import com.ynab.ui.addAccount.AddAccount
import com.ynab.ui.theme.YNABTheme
import com.ynab.ui.login.Login
import com.ynab.ui.register.Register
import com.ynab.ui.settings.Settings
import com.ynab.ui.splash.Splash
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

    val topLevelRoutes = listOf(
        TopLevelRoute("Accounts", Accounts, Icons.Default.AccountBalance),
        TopLevelRoute("Settings", Settings, Icons.Default.Settings)
    )

    val bottomNavBar: @Composable () -> Unit = {
        NavigationBar {
            topLevelRoutes.forEach { topLevelRoute ->
                NavigationBarItem(
                    icon = { Icon(topLevelRoute.icon, contentDescription = topLevelRoute.name) },
                    label = { Text(topLevelRoute.name) },
                    selected =
                    navController.currentBackStackEntry?.
                    destination?.hierarchy?.any {
                        it.hasRoute(topLevelRoute.route::class)
                    } == true,
                    onClick = {
                        navController.navigate(
                            route = topLevelRoute.route,
                            builder = {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // re-selecting the same item
                                launchSingleTop = true
                                // Restore state when re-selecting a previously selected item
                                restoreState = true
                            }
                        )
                    }
                )
            }
        }
    }

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
                modifier = modifier,
                isNewUser = (backStackEntry.toRoute() as Splash).isNewUser,
                onStartupComplete = { navController.navigate(route = Settings) }
            )
        }
        composable<Settings> {
            Settings(
                modifier = modifier,
                bottomNavBar = bottomNavBar
            )
        }
        composable<Accounts> {
            Accounts(
                modifier = modifier,
                bottomNavBar = bottomNavBar,
                onAccountClicked = { Log.d(TAG, "Account ${it.accountName} clicked") },
                onAllTransactionsClicked = { Log.d(TAG, "All transactions clicked") },
                onAddAccountClicked = { navController.navigate(route = AddAccount) }
            )
        }
        composable<AddAccount> {
            AddAccount(
                modifier = modifier,
                onAddAccountComplete = { navController.popBackStack() }
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