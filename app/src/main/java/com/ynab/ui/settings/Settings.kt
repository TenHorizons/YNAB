package com.ynab.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Settings(
    modifier: Modifier,
    bottomNavBar: @Composable () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = bottomNavBar
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) { Text("Hello") }
    }
}