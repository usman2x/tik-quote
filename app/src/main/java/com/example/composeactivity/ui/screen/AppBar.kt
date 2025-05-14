package com.example.composeactivity.ui.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String, onSyncClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF16213E), // Dark background
            titleContentColor = Color.White
        ),
        modifier = Modifier.fillMaxWidth(),
        actions = {
            IconButton(onClick = onSyncClick) {
                Icon(Icons.Default.Refresh, contentDescription = "Sync", tint = Color.White)
            }
        }
    )
}