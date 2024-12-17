package com.ynab.ui.settings

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    modifier: Modifier,
    vm: SettingsViewModel = hiltViewModel(),
    bottomNavBar: @Composable () -> Unit,
) {
    val context = LocalContext.current as Activity
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("Settings", maxLines = 1, overflow = TextOverflow.Ellipsis) }) },
        bottomBar = bottomNavBar
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                items(listOf("Delete Account")) { item ->
                    Card {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(8.dp,12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            TextButton(onClick = {vm.deleteAccount(onDeleteComplete = {
                                val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)!!
                                val componentName = intent.component!!
                                val restartIntent = Intent.makeRestartActivityTask(componentName)
                                context.startActivity(restartIntent)
                                Runtime.getRuntime().exit(0)
                            })}){ Text(item) }
                        }
                    }
                }
            }
        }
    }
}