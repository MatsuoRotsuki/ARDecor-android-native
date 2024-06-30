package com.soictnative.ardecor.presentation.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.soictnative.ardecor.ARDecorUnityActivity
import com.soictnative.ardecor.R
import com.soictnative.ardecor.data.manager.AppConnectivityManager
import com.soictnative.ardecor.data.manager.ConnectionState
import com.soictnative.ardecor.presentation.navigation.Route
import com.soictnative.ardecor.presentation.screens.ideas.NewIdeaRoute
import com.soictnative.ardecor.presentation.screens.ideas.NewIdeaViewModel
import com.soictnative.ardecor.presentation.screens.save_placement.save_current.SavedPlacementByUserRoute
import com.soictnative.ardecor.presentation.screens.save_placement.save_current.SavedPlacementByUserViewModel
import com.soictnative.ardecor.presentation.screens.save_placement.save_override.SavePlacementOverrideRoute
import com.soictnative.ardecor.presentation.screens.save_placement.save_override.SavePlacementOverrideViewModel
import com.soictnative.ardecor.presentation.theme.ApplicationTheme
import com.soictnative.ardecor.util.ScreenState
import com.soictnative.ardecor.presentation.viewmodel.SavePlacementViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SavePlacementActivity : ComponentActivity() {

    private val rootViewModel by viewModels<SavePlacementViewModel>()

    @Inject
    lateinit var appConnectivityManager: AppConnectivityManager

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.extras == null) return

        if (intent.extras!!.containsKey("destinationPage")) {
            val destinationPage = intent.extras!!.getString("destinationPage")
            rootViewModel.changeScreenTo(destinationPage!!)
        }

        if(intent.extras!!.containsKey("placementJson")) {
            val placementJson = intent.extras!!.getString("placementJson")
            rootViewModel.updatePlacementJson(placementJson!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent: Intent = intent
        handleIntent(intent)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val navController = rememberNavController()
                    val scope = rememberCoroutineScope()
                    val snackbarHostState = remember { SnackbarHostState() }
                    val context = LocalContext.current
                    val noInternetConnectionText = stringResource(id = R.string.no_internet_connection)
                    LaunchedEffect(key1 = true) {
                        appConnectivityManager.connectionState.collect {
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
                        modifier = Modifier.fillMaxSize()
                    ) { paddingValues ->
                        val bottomPadding = paddingValues.calculateBottomPadding()
                        NavHost(
                            navController = navController,
                            startDestination = rootViewModel.startDestination.value,
                            modifier = Modifier.padding(bottom = bottomPadding)
                        ) {
                            navigation(
                                route = Route.SavePlacementOverride.route,
                                startDestination = "route1"
                            ) {
                                composable(route = "route1") {
                                    val viewModel: SavePlacementOverrideViewModel = hiltViewModel()
                                    val placementState by viewModel.placements.observeAsState()
                                    viewModel.newPlacement.observeForever {
                                        when(it) {
                                            is ScreenState.Loading -> {

                                            }
                                            is ScreenState.Error -> {
                                                scope.launch {
                                                    snackbarHostState.showSnackbar(it.message, duration = SnackbarDuration.Short)
                                                }
                                            }
                                            is ScreenState.Success -> {
                                                (context as ComponentActivity).finish()
                                            }
                                            else -> Unit
                                        }
                                    }
                                    SavePlacementOverrideRoute(
                                        placementState = placementState,
                                        selectedPlacement = viewModel.selectedPlacement.value,
                                        onOptionSelected = viewModel::onOptionSelected,
                                        onError = {
                                            scope.launch {
                                                snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Long)
                                            }
                                        },
                                        onConfirm = {
                                            val dialog = AlertDialog.Builder(context)
                                                .setMessage("Bạn có muốn viết đè không")
                                                .setPositiveButton("OK") { dialog, which ->
                                                    val placementJson = rootViewModel.placementJson.value
                                                    viewModel.savePlacementOverride(placementJson)
                                                    dialog.dismiss()
                                                }
                                                .setNegativeButton("Huỷ bỏ") { dialog, which ->
                                                    dialog.dismiss()
                                                }

                                            dialog.show()
                                        },
                                        onRefresh = viewModel::getAllSavedPlacementsByUser,
                                    )
                                }
                            }
                            navigation(
                                route = Route.SavedPlacementByUser.route,
                                startDestination = "route2"
                            ) {
                                composable(route = "route2") {
                                    val viewModel: SavedPlacementByUserViewModel = hiltViewModel()
                                    val placementState by viewModel.placements.observeAsState(initial = ScreenState.Loading)
                                    viewModel.deleteTask.observeForever {
                                        when(it) {
                                            is ScreenState.Loading -> {

                                            }
                                            is ScreenState.Error -> {
                                                scope.launch {
                                                    snackbarHostState.showSnackbar(it.message, duration = SnackbarDuration.Short)
                                                }
                                            }
                                            is ScreenState.Success -> {
                                                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show()
                                                viewModel.getAllSavedPlacementsByUser()
                                            }
                                            else -> Unit
                                        }
                                    }
                                    SavedPlacementByUserRoute(
                                        placementState = placementState,
                                        onClickInitialize = { placement ->
                                            Intent(context, ARDecorUnityActivity::class.java).also { intent ->
                                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                                                intent.putExtra("initializePlacement", placement.placements)
                                                context.startActivity(intent)
                                            }
                                        },
                                        onClickStartAR = {
                                            Intent(context, ARDecorUnityActivity::class.java).also { intent ->
                                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                                                context.startActivity(intent)
                                            }
                                        },
                                        onClickDelete = { placement ->
                                            viewModel.deleteSavedPlacement(placement)
                                        },
                                        onError = {
                                            scope.launch {
                                                snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Long)
                                            }
                                        },
                                        onRefresh = viewModel::getAllSavedPlacementsByUser,
                                        onClickCreateIdea = { savedPlacement ->
                                            rootViewModel.setPlacementData(savedPlacement)
                                            navController.navigate(Route.NewIdeaScreen.route)
                                        }
                                    )
                                }
                            }
                            navigation(
                                route = Route.NewIdeaScreen.route,
                                startDestination = "route3"
                            ) {
                                composable(route = "route3") {
                                    val placementData = rootViewModel.placementDatatoCreateIdea.value
                                    val viewModel: NewIdeaViewModel = hiltViewModel()
                                    val roomTypeState by viewModel.roomTypes.observeAsState(initial = ScreenState.Loading)
                                    viewModel.newIdea.observeForever {
                                        when(it) {
                                            is ScreenState.Loading -> Toast.makeText(context, "Creating new idea...", Toast.LENGTH_SHORT).show()
                                            is ScreenState.Error -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                                            is ScreenState.Success -> {
                                                Toast.makeText(context, "Created new idea successfully", Toast.LENGTH_SHORT).show()
                                                finish()
                                            }
                                            else -> Unit
                                        }
                                    }
                                    NewIdeaRoute(
                                        roomTypeState = roomTypeState,
                                        savedPlacement = placementData,
                                        onSubmit = viewModel::onSubmit
                                    ) {
                                        navController.navigateUp()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}