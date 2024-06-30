package com.soictnative.ardecor.presentation.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.gson.Gson
import com.soictnative.ardecor.ARDecorUnityActivity
import com.soictnative.ardecor.R
import com.soictnative.ardecor.data.dto.Category
import com.soictnative.ardecor.data.dto.Idea
import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.data.manager.AppConnectivityManager
import com.soictnative.ardecor.data.manager.ConnectionState
import com.soictnative.ardecor.presentation.navigation.AppBottomNavBar
import com.soictnative.ardecor.presentation.navigation.BottomNavItem
import com.soictnative.ardecor.presentation.navigation.Route
import com.soictnative.ardecor.presentation.screens.categories.CategoryDetailRoute
import com.soictnative.ardecor.presentation.screens.home.HomeRoute
import com.soictnative.ardecor.presentation.screens.home.HomeViewModel
import com.soictnative.ardecor.presentation.screens.ideas.IdeaHomeRoute
import com.soictnative.ardecor.presentation.screens.ideas.IdeaHomeViewModel
import com.soictnative.ardecor.presentation.screens.product_details.ProductDetailsRoute
import com.soictnative.ardecor.presentation.screens.product_details.ProductDetailsViewModel
import com.soictnative.ardecor.presentation.screens.profile.ProfileRoute
import com.soictnative.ardecor.presentation.screens.profile.ProfileViewModel
import com.soictnative.ardecor.presentation.screens.search.SearchRoute
import com.soictnative.ardecor.presentation.screens.search.SearchViewModel
import com.soictnative.ardecor.presentation.theme.ApplicationTheme
import com.soictnative.ardecor.util.ScreenState
import com.soictnative.ardecor.presentation.screens.categories.CategoryDetailsViewModel
import com.soictnative.ardecor.presentation.screens.favourites.FavouriteRoute
import com.soictnative.ardecor.presentation.screens.favourites.FavouriteViewModel
import com.soictnative.ardecor.presentation.screens.ideas.IdeaDetailRoute
import com.soictnative.ardecor.presentation.screens.ideas.IdeaDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    @Inject
    lateinit var appConnectivityManager: AppConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ApplicationTheme {
                App(
                    connectivityManager = appConnectivityManager
                )
            }
        }
    }
}

@Composable
fun App(
    modifier: Modifier = Modifier,
    connectivityManager: AppConnectivityManager,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        val navController = rememberNavController()

        val navBackStackEntry by navController.currentBackStackEntryAsState()

        var selectedItem by rememberSaveable { mutableIntStateOf(0) }
        selectedItem = when (navBackStackEntry?.destination?.route) {
            Route.HomeScreen.route -> 0
            Route.SearchScreen.route -> 1
            Route.IdeaHomeScreen.route -> 2
            Route.ProfileScreen.route -> 3
            else -> 0
        }
        val bottomNavItems = listOf(
            BottomNavItem.Home,
            BottomNavItem.Search,
            BottomNavItem.Idea,
            BottomNavItem.Profile
        )

        val isBottomBarVisible = remember(key1 = navBackStackEntry) {
            navBackStackEntry?.destination?.route == Route.HomeScreen.route ||
                    navBackStackEntry?.destination?.route == Route.SearchScreen.route ||
                    navBackStackEntry?.destination?.route == Route.ProfileScreen.route ||
                    navBackStackEntry?.destination?.route == Route.IdeaHomeScreen.route
        }

        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        val context = LocalContext.current

        val noInternetConnectionText = stringResource(id = R.string.no_internet_connection)
        LaunchedEffect(key1 = true) {
            connectivityManager.connectionState.collect {
                when (it) {
                    ConnectionState.Connected -> {
                        snackbarHostState.currentSnackbarData?.dismiss()
                    }
                    ConnectionState.NotConnected -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                noInternetConnectionText,
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                    }
                    ConnectionState.Unknown -> {
                        snackbarHostState.currentSnackbarData?.dismiss()
                    }
                }
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (isBottomBarVisible) {
                    AppBottomNavBar(
                        items = bottomNavItems,
                        selectedItem = selectedItem,
                        onItemClick = { index ->
                            when (index) {
                                0 -> navigateToTab(
                                    navController = navController,
                                    route = Route.HomeScreen.route
                                )
                                1 -> navigateToTab(
                                    navController = navController,
                                    route = Route.SearchScreen.route
                                )
                                2-> navigateToTab(
                                    navController = navController,
                                    route = Route.IdeaHomeScreen.route
                                )
                                3 -> navigateToTab(
                                    navController = navController,
                                    route = Route.ProfileScreen.route
                                )
                            }
                        }
                    )
                }
            }
        ) { paddingValues ->
            val bottomPadding = paddingValues.calculateBottomPadding()
            NavHost(
                navController = navController,
                startDestination = Route.HomeScreen.route,
                modifier = Modifier.padding(bottom = bottomPadding)
            ) {
                composable(route = Route.HomeScreen.route) {
                    val viewModel: HomeViewModel = hiltViewModel()
                    val productState by viewModel.products.observeAsState(initial = ScreenState.Loading)
                    val recentlyViewedProductState by viewModel.recentlyViewedProducts.observeAsState(initial = ScreenState.Loading)
                    val categoriesState by viewModel.categories.observeAsState(initial = ScreenState.Loading)
                    val products = viewModel.pagingProducts.collectAsLazyPagingItems()
                    HomeRoute(
                        productsState = productState,
                        recentlyViewedProductState = recentlyViewedProductState,
                        categoriesState = categoriesState,
                        onProductClicked = { productId ->
                            navigateToProductDetail(
                                productId = productId,
                                navController = navController
                            )
                        },
                        onCategoryClicked = { category ->
                            navigateToCategoryDetail(
                                category = category,
                                navController = navController
                            )
                        },
                        onClickSearch = {
                            navigateToTab(
                                navController,
                                Route.SearchScreen.route
                            )
                        },
                        onError = {
                            scope.launch {
                                snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
                            }
                        },
                        onRefresh = {
                            viewModel.refresh()
                        },
                        //
                        products = products
                        //
                    )
                }
                composable(route = Route.ProductDetailScreen.routeWithArgs, arguments = Route.ProductDetailScreen.arguments) {
                    val viewModel: ProductDetailsViewModel = hiltViewModel()
                    val productState by viewModel.product.observeAsState(initial = ScreenState.Loading)
                    val recentlyViewedProductState by viewModel.recentlyViewedProducts.observeAsState(initial = ScreenState.Loading)
                    val userUid by viewModel.userUid.observeAsState()
                    ProductDetailsRoute(
                        productState = productState,
                        recentlyViewedProductState = recentlyViewedProductState,
                        onClickVariation = { productId ->
                            viewModel.switchProduct(productId)
                        },
                        onClickViewAR = { product ->
                            val gson = Gson()
                            val productJson = gson.toJson(product)
                            Intent(context, ARDecorUnityActivity::class.java).also { intent ->
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                                intent.putExtra("initializeProduct", productJson)
                                intent.putExtra("UserUid", userUid)
                                context.startActivity(intent)
                            }
                        },
                        onClickView3d = { product ->
                            viewModel.getDownloadUrl(
                                fileReference = product.model_path ?: "",
                                onSuccess = { url -> //#val encodedUrl = url.replace("&", "%26").replace("%", "%25")========
                                    val intentUri = Uri.parse("https://arvr.google.com/scene-viewer/1.0").buildUpon()
                                        .appendQueryParameter("file", url)
                                        .appendQueryParameter("mode", "3d_preferred")
                                        .appendQueryParameter("link", product.source)
                                        .appendQueryParameter("title", product.name)
                                        .build()
                                    Intent(Intent.ACTION_VIEW).also { intent ->
                                        intent.setData(intentUri)
                                        intent.setPackage("com.google.android.googlequicksearchbox")
                                        context.startActivity(intent)
                                    }
                                },
                                onFailure = { exception ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(exception.message!!, duration = SnackbarDuration.Short)
                                    }
                                })
                        },
                        onClickAddToFavourite = { product ->
                            viewModel.addProductToFavourite(product)
                        },
                        onClickBack = {
                            navController.navigateUp()
                        },
                        onError = {
                            scope.launch {
                                snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
                            }
                        }
                    )
                }
                composable(route = Route.CategoryDetailScreen.routeWithArgs, arguments = Route.CategoryDetailScreen.arguments) {
                    val viewModel: CategoryDetailsViewModel = hiltViewModel()
                    val categoryState by viewModel.category.observeAsState(initial = ScreenState.Loading)
                    CategoryDetailRoute(
                        categoryState = categoryState,
                        onProductClicked = { product ->
                            navigateToProductDetail(
                                productId = product.id,
                                navController = navController
                            )
                        },
                        onError = {
                            scope.launch {
                                snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
                            }
                        },
                        onRefresh = viewModel::fetchProducts
                    )
                }
                composable(route = Route.SearchScreen.route) {
                    val viewModel: SearchViewModel = hiltViewModel()
                    val productState by viewModel.products.collectAsState()
                    val searchText by viewModel.searchText.collectAsState()
                    val sortState by viewModel.sort.collectAsState()
                    val minPriceState by viewModel.minPrice.collectAsState()
                    val maxPriceState by viewModel.maxPrice.collectAsState()
                    SearchRoute(
                        searchText = searchText,
                        productState = productState,
                        sortState = sortState,
                        minPriceState = minPriceState,
                        maxPriceState = maxPriceState,
                        onSortChanged = viewModel::onSortChanged,
                        onPriceRangeChosen = viewModel::onPriceRangeChosen,
                        onError = {
                            scope.launch {
                                snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
                            }
                        },
                        onProductClicked = { product ->
                            navigateToProductDetail(
                                productId = product.id,
                                navController = navController
                            )
                        },
                        onSearchTextChanged = viewModel::onSearchTextChanged,
                        onClickBack = {
                            navController.navigateUp()
                        }
                    )
                }
                composable(route = Route.IdeaHomeScreen.route) {
                    val viewModel: IdeaHomeViewModel = hiltViewModel()
                    val roomState by viewModel.roomType.observeAsState()
                    val ideaState by viewModel.idea.observeAsState()
                    IdeaHomeRoute(
                        roomState = roomState,
                        onRoomTypeClicked = {},
                        onIdeaClicked = { idea ->
                            navigateToIdeaDetail(navController, idea)
                        },
                        onError = {
                            scope.launch {
                                snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
                            }
                        },
                        onRefresh = viewModel::getAllIdeas,
                        onClickAdd = {
                            Intent(context, SavePlacementActivity::class.java).also { intent ->
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                                intent.putExtra("destinationPage", Route.SavedPlacementByUser.route)
                                context.startActivity(intent)
                            }
                        },
                        ideaState = ideaState
                    )
                }
                composable(route = Route.IdeaDetailScreen.routeWithArgs, arguments = Route.IdeaDetailScreen.arguments) {
                    val viewModel: IdeaDetailViewModel = hiltViewModel()
                    val ideaState by viewModel.idea.observeAsState()
                    val userUid by viewModel.userUid.observeAsState()
                    IdeaDetailRoute(
                        ideaState = ideaState,
                        onClickInitialize = { idea ->
                            Intent(context, ARDecorUnityActivity::class.java).also { intent ->
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                                intent.putExtra("UserUid", userUid)
                                intent.putExtra("initializePlacement", idea.placements)
                                context.startActivity(intent)
                            }
                        },
                        onClickProduct = { product ->
                            navigateToProductDetail(navController, product.id)
                        },
                        onClickBack = {
                            navController.navigateUp()
                        },
                        onError = { message ->
                            scope.launch {
                                snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
                            }
                        }
                    )
                }
                composable(route = Route.ProfileScreen.route) {
                    val viewModel: ProfileViewModel = hiltViewModel()
                    val user by viewModel.user.observeAsState()
                    ProfileRoute(
                        profileState = user,
                        onClickUpdateProfile = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Chức năng này chưa có sẵn", duration = SnackbarDuration.Short)
                            }
                        },
                        onClickFavouriteCollection = {
                             navController.navigate(route = Route.FavouriteProductsScreen.route)
                        },
                        onClickSavedPlacements = {
                            Intent(context, SavePlacementActivity::class.java).also { intent ->
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                                intent.putExtra("destinationPage", Route.SavedPlacementByUser.route)
                                context.startActivity(intent)
                            }
                        },
                        onClickLogOut = {
                            viewModel.signOut()
                            Intent(context, LoginRegisterActivity::class.java).also { intent ->
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(intent)
                            }
                        },
                        onError = { message ->
                            scope.launch {
                                snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
                            }
                        }
                    )
                }
                composable(route = Route.ProfileUpdateScreen.route) {

                }
                composable(route = Route.FavouriteProductsScreen.route) {
                    val viewModel: FavouriteViewModel = hiltViewModel()
                    val favouriteProductState by viewModel.favouriteProducts.observeAsState(initial = ScreenState.Loading)
                    FavouriteRoute(
                        favouriteProductState = favouriteProductState,
                        onClickProduct = { productId ->
                            navigateToProductDetail(navController, productId)
                        },
                        onClickDelete = viewModel::removeFromFavouriteProducts,
                        onRefresh = viewModel::refresh,
                        onClickBack = {
                            navController.navigateUp()
                        },
                        onError = { message ->
                            scope.launch {
                                snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
                            }
                        }
                    )
                }
            }
        }
    }
}

private fun navigateToTab(
    navController: NavController,
    route: String
) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { screen_route ->
            popUpTo(screen_route) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

private fun navigateToProductDetail(
    navController: NavController,
    productId: Int
) {
    val route = "${Route.ProductDetailScreen.route}/${productId}"
    navController.navigate(route = route)
}

private fun navigateToCategoryDetail(
    navController: NavController,
    category: Category
) {
    val route = "${Route.CategoryDetailScreen.route}/${category.id}"
    navController.navigate(route = route)
}

private fun navigateToIdeaDetail(
    navController: NavController,
    idea: Idea
) {
    val route = "${Route.IdeaDetailScreen.route}/${idea.id}"
    navController.navigate(route = route)
}

@Composable
fun OnBackClickStateSaver(navController: NavController) {
    BackHandler(true) {
        navigateToTab(
            navController = navController,
            route = Route.HomeScreen.route
        )
    }
}

