package com.example.composeactivity.ui.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.composeactivity.ui.theme.AppBarColors
import com.example.composeactivity.ui.theme.AppBarTitleTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    isSearchEnabled: Boolean = false,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onSearchToggle: () -> Unit = {},
    onSyncClick: () -> Unit
) {
    var isSearching by rememberSaveable { mutableStateOf(false) }

    TopAppBar(
        title = {
            if (isSearchEnabled && isSearching) {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Search...", color = AppBarColors.SearchPlaceholder) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = AppBarColors.SearchText,
                        unfocusedTextColor = AppBarColors.SearchText,
                        cursorColor = AppBarColors.SearchText,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = AppBarColors.SearchFocusedIndicator,
                        unfocusedIndicatorColor = AppBarColors.SearchUnfocusedIndicator,
                        focusedPlaceholderColor = AppBarColors.SearchPlaceholder,
                        unfocusedPlaceholderColor = AppBarColors.SearchPlaceholder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = title,
                    style = AppBarTitleTextStyle
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppBarColors.Background, // Background color for AppBar
            titleContentColor = Color.White
        ),
        modifier = Modifier.fillMaxWidth(),
        actions = {
            if (isSearchEnabled) {
                IconButton(onClick = {
                    isSearching = !isSearching
                    if (!isSearching) onSearchQueryChange("")
                    onSearchToggle()
                }) {
                    Icon(
                        imageVector = if (isSearching) Icons.Default.Close else Icons.Default.Search,
                        contentDescription = "Toggle Search",
                        tint = Color.White
                    )
                }
            }
            IconButton(onClick = onSyncClick) {
                Icon(Icons.Default.Refresh, contentDescription = "Sync", tint = Color.White)
            }
        }
    )
}