package com.example.composeactivity.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Favorite as FavoriteIcon
import androidx.compose.material.icons.filled.Home as HomeIcon
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.composeactivity.data.DataStoreManager
import com.example.composeactivity.ui.theme.CategoryUnselected
import com.example.composeactivity.viewmodel.QuoteViewModel
import com.example.composeactivity.viewmodel.SettingsViewModel


sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object Quotes : BottomNavItem("quotes", Icons.Default.HomeIcon, "Quotes")
    data object Search : BottomNavItem("search", Icons.Default.Search, "Search")
    data object Liked : BottomNavItem("liked", Icons.Default.FavoriteIcon, "Liked")
    data object Saved : BottomNavItem("saved", Icons.Default.AddCircle, "Saved")
    data object Settings : BottomNavItem("settings", Icons.Default.Settings, "Settings")
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Quotes,
        BottomNavItem.Search,
        BottomNavItem.Liked,
        BottomNavItem.Saved,
        BottomNavItem.Settings
    )
    NavigationBar(
        containerColor = CategoryUnselected
    ) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(text = item.label) },
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Gray
                ),
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController, viewModel: QuoteViewModel) {
    val context = LocalContext.current

    val categories = viewModel.categories.sorted()

    val dataStoreManager = remember { DataStoreManager(context) }

    val settingsViewModel = remember(categories) {
        SettingsViewModel(dataStoreManager, categories)
    }

    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Quotes.route
    ) {
        composable(BottomNavItem.Quotes.route) {
            QuoteScreen(viewModel, settingsViewModel)
        }
        composable(BottomNavItem.Search.route) {
            SearchScreen(viewModel)
        }
        composable(BottomNavItem.Liked.route) {
            LikedQuoteScreen(viewModel)
        }
        composable(BottomNavItem.Saved.route) {
            SavedQuoteScreen(viewModel)
        }
        composable(BottomNavItem.Settings.route) {
            val selectedCategories by settingsViewModel.selectedCategories.collectAsState()
            SettingsScreen(
                allCategories = categories,
                selectedCategories = selectedCategories.toList(),
                onCategoryToggle = settingsViewModel::toggleCategory,
                onSelectAllToggle = settingsViewModel::setAllCategories
            )
        }
    }
}
