package com.soictnative.ardecor.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String,
) {
    object Home: BottomNavItem(
        title = "Home",
        icon = Icons.Default.Home,
        route = Route.HomeScreen.route
    )

    object Search: BottomNavItem(
        title = "Search",
        icon = Icons.Default.Search,
        route = Route.SearchScreen.route
    )

    object Idea: BottomNavItem(
        title = "Ideabooks",
        icon = Icons.Default.Category,
        route = Route.IdeaHomeScreen.route
    )

    object Profile: BottomNavItem(
        title = "Profile",
        icon = Icons.Default.AccountCircle,
        route = Route.ProfileScreen.route
    )
}