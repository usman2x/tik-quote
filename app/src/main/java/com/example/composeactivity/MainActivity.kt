package com.example.composeactivity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.composeactivity.data.QuoteDatabase
import com.example.composeactivity.data.QuoteRepository
import com.example.composeactivity.data.RetrofitInstance
import com.example.composeactivity.ui.screen.AppBar
import com.example.composeactivity.ui.screen.AppNavHost
import com.example.composeactivity.ui.screen.BottomNavItem
import com.example.composeactivity.ui.screen.BottomNavigationBar
import com.example.composeactivity.ui.theme.ComposeActivityTheme
import com.example.composeactivity.viewmodel.QuoteViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = QuoteDatabase.getDatabase(this)
        val repository = QuoteRepository(database.quoteDao(), RetrofitInstance.api)
        val viewModel = QuoteViewModel(repository)

        setContent {
            ComposeActivityTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MainScreen(viewModel)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: QuoteViewModel) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: BottomNavItem.Quotes.route

    val isSearchScreen = currentRoute == BottomNavItem.Search.route

    val searchQuery by viewModel.searchQuery.collectAsState()


    val title = when (currentRoute) {
        BottomNavItem.Quotes.route -> "All Quotes"
        BottomNavItem.Search.route -> "Search Quotes"
        BottomNavItem.Liked.route -> "Liked Quotes"
        BottomNavItem.Saved.route -> "Saved Quotes"
        BottomNavItem.Settings.route -> "Settings"
        else -> "Quotes"
    }

    Scaffold(
        topBar = {
            AppBar(
                title = if (isSearchScreen) "Search Quotes" else title,
                isSearchEnabled = isSearchScreen,
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                onSearchToggle = { viewModel.updateSearchQuery("") },
                onSyncClick = {
                    viewModel.syncQuotes {
                        Toast.makeText(context, "Sync Completed!", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)){
            AppNavHost(navController = navController, viewModel = viewModel)
        }
    }
}