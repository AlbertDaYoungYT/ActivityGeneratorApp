package threeax.productivity.ideagen.ui.screens

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import threeax.productivity.ideagen.R
import threeax.productivity.ideagen.core.BottomSheetStateModal
import threeax.productivity.ideagen.core.DialogNewTask
import threeax.productivity.ideagen.core.NewTaskViewModel
import threeax.productivity.ideagen.core.Settings
import threeax.productivity.ideagen.persistence.ActivityDBHandler
import threeax.productivity.ideagen.persistence.ActivityModel
import threeax.productivity.ideagen.ui.components.TopNavBar
import threeax.productivity.ideagen.ui.pages.ArchiveScreen
import threeax.productivity.ideagen.ui.pages.FeedScreen
import threeax.productivity.ideagen.ui.pages.HomeScreen


sealed class MainScreen(val route: String, @StringRes val resourceId: Int, @DrawableRes val iconId: Int) {
    object Home : MainScreen("home", R.string.home, R.drawable.home_24px)
    object Feed : MainScreen("feed", R.string.feed, R.drawable.newspaper_24px)
    object Archive : MainScreen("archive", R.string.archive, R.drawable.inventory_24px)
}

val items = listOf(
    MainScreen.Home,
    MainScreen.Feed,
    MainScreen.Archive,
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    context: Context,
    activityPressed: (Int?) -> Unit,
    onSettingsClick: () -> Unit,
    settings: Settings
) {
    val navController = rememberNavController()

    // on below line we are creating and initializing our array list
    lateinit var activityList: List<ActivityModel>
    activityList = ArrayList<ActivityModel>()

    val activityDbHandler: ActivityDBHandler = ActivityDBHandler(context)
    activityList = activityDbHandler.readActivities()

    val openAlertDialog = remember { mutableStateOf(false) }
    val openEditDialog = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val viewModel = remember { NewTaskViewModel() }


    val sheetState = rememberModalBottomSheetState()
    val showBottomSheet = remember { BottomSheetStateModal() }


    if (openAlertDialog.value) {
        DialogNewTask(
            onDismissRequest = { openAlertDialog.value = false },
            onConfirmation = {
                openAlertDialog.value = false
                activityDbHandler.addNewActivity(
                    viewModel.title,
                    viewModel.description
                )
                scope.launch {
                    snackbarHostState.showSnackbar("Created " + viewModel.title)
                }
            },
            dialogTitle = "Create new Activity",
            value = viewModel
        )
    }

    if (openEditDialog.value) {
        viewModel.title = activityList[showBottomSheet.activityIndex].activityName
        viewModel.description = activityList[showBottomSheet.activityIndex].activityDescription

        DialogNewTask(
            onDismissRequest = { openEditDialog.value = false },
            onConfirmation = {
                openEditDialog.value = false
                scope.launch {
                    activityDbHandler.editActivity(
                        activityList[showBottomSheet.activityIndex].activityId,
                        viewModel.title,
                        viewModel.description
                    )
                    snackbarHostState.showSnackbar("Edited "+viewModel.title)
                }
            },
            dialogTitle = "Edit Activity",
            value = viewModel
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopNavBar(
                context,
                settings,
                onSettingsClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                openAlertDialog.value = true
            }) {
                Icon(Icons.Filled.Add, "New")
            }
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(painter = painterResource(screen.iconId), contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(
                                screen.route
                            ) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = MainScreen.Home.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(
                MainScreen.Home.route
            ) {
                HomeScreen(
                    context,
                    navController,
                    activityDbHandler,
                    settings,
                    showBottomSheet,
                    activityPressed
                )
            }
            composable(
                MainScreen.Feed.route
            ) {
                FeedScreen(
                    context,
                    navController,
                    activityPressed
                )
            }
            composable(
                MainScreen.Archive.route
            ) {
                ArchiveScreen(
                    context,
                    navController,
                    activityPressed
                )
            }
        }

        if (showBottomSheet.showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet.showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Column {
                    ListItem(
                        headlineContent = { Text("View") },
                        leadingContent = {
                            Icon(
                                Icons.Filled.MoreVert,
                                contentDescription = "Localized description"
                            )
                        },
                        modifier = Modifier.clickable {
                            showBottomSheet.showBottomSheet = false
                            activityPressed(showBottomSheet.activityId)
                        }
                    )
                    ListItem(
                        headlineContent = { Text("Complete") },
                        leadingContent = {
                            Icon(
                                Icons.Filled.Done,
                                contentDescription = "Localized description"
                            )
                        },
                        modifier = Modifier.clickable {
                            showBottomSheet.showBottomSheet = false
                        }
                    )
                    ListItem(
                        headlineContent = { Text("Reroll") },
                        leadingContent = {
                            Icon(
                                Icons.Filled.Refresh,
                                contentDescription = "Localized description"
                            )
                        }
                    )
                    ListItem(
                        headlineContent = { Text("Edit") },
                        leadingContent = {
                            Icon(
                                Icons.Filled.Create,
                                contentDescription = "Localized description"
                            )
                        },
                        modifier = Modifier.clickable {
                            showBottomSheet.showBottomSheet = false
                            openEditDialog.value = true
                        }
                    )
                    ListItem(
                        headlineContent = { Text("Archive") },
                        leadingContent = {
                            Icon(
                                painterResource(id = R.drawable.inventory_24px),
                                contentDescription = "Localized description"
                            )
                        },
                        modifier = Modifier.clickable {
                            showBottomSheet.showBottomSheet = false
                        }
                    )
                    ListItem(
                        headlineContent = { Text("Delete") },
                        leadingContent = {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Localized description"
                            )
                        },
                        modifier = Modifier.clickable {
                            activityDbHandler.deleteActivity(activityList[showBottomSheet.activityIndex].activityId)
                            showBottomSheet.showBottomSheet = false
                        }
                    )
                }
            }
        }
    }
}