package com.soictnative.ardecor.presentation.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Route (
    val route: String
) {
    object OnBoardingScreen: Route(route = "onBoardingScreen")
    object HomeScreen: Route(route = "homeScreen")
    object SearchScreen: Route(route = "searchScreen")
    object ProductDetailScreen : Route(route = "productDetailScreen") {
        private const val PRODUCT_ID = "productId"
        val routeWithArgs = "$route/{$PRODUCT_ID}"
        val arguments = listOf(
            navArgument(PRODUCT_ID) { type = NavType.IntType }
        )
    }

    object CategoryDetailScreen: Route(route = "categoryDetailScreen") {
        private const val CATEGORY_ID = "categoryId"
        val routeWithArgs = "$route/{$CATEGORY_ID}"
        val arguments = listOf(
            navArgument(CATEGORY_ID) { type = NavType.IntType }
        )
    }
    object IdeaHomeScreen: Route(route = "ideaHomeScreen")
    object IdeaDetailScreen: Route(route = "ideaRouteScreen") {
        private const val IDEA_ID = "ideaId"
        val routeWithArgs = "$route/{$IDEA_ID}"
        val arguments = listOf(
            navArgument(IDEA_ID) { type = NavType.IntType }
        )
    }
    object NewIdeaScreen: Route(route = "newIdeaScreen")
    object ProfileScreen: Route(route = "profileScreen")
    object ProfileUpdateScreen: Route(route = "profileUpdateScreen")
    object AppNavigation: Route(route = "appNavigation")

    object SavePlacementOverride: Route(route = "savePlacementOverride")

    object SavedPlacementByUser: Route(route = "savedPlacementByUser")

    object FavouriteProductsScreen: Route(route = "favouriteProductsScreen")
}