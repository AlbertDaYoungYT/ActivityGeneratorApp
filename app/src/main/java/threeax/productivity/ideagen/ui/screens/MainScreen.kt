package threeax.productivity.ideagen.ui.screens

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import threeax.productivity.ideagen.R
import threeax.productivity.ideagen.core.DialogNewTask
import threeax.productivity.ideagen.core.NewTaskViewModel
import threeax.productivity.ideagen.pages.ArchiveScreen
import threeax.productivity.ideagen.pages.FeedScreen
import threeax.productivity.ideagen.pages.HomeScreen
import threeax.productivity.ideagen.ui.components.TopNavBar


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


@Composable
fun MainScreen(
    context: Context,
    navController: NavHostController
) {
    val openAlertDialog = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val viewModel = remember { NewTaskViewModel() }

    if (openAlertDialog.value) {
        DialogNewTask(
            onDismissRequest = { openAlertDialog.value = false },
            onConfirmation = {
                openAlertDialog.value = false
                scope.launch {
                    snackbarHostState.showSnackbar("Creating "+viewModel.title)
                }
            },
            dialogTitle = "Create new Activity",
            value = viewModel
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopNavBar()
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
                HomeScreen(context, navController)
            }
            composable(
                MainScreen.Feed.route
            ) {
                FeedScreen(context, navController)
            }
            composable(
                MainScreen.Archive.route
            ) {
                ArchiveScreen(context, navController)
            }
        }
    }

}