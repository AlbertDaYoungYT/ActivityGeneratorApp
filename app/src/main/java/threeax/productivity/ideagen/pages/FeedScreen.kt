package threeax.productivity.ideagen.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import threeax.productivity.ideagen.core.NewTaskViewModel
import threeax.productivity.ideagen.ui.components.NavigationBarKt
import threeax.productivity.ideagen.ui.components.TopNavBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen() {
    val openAlertDialog = remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }


    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopNavBar()
        },
        bottomBar = {
            NavigationBarKt()
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                openAlertDialog.value = true
            }) {
                Icon(Icons.Filled.Add, "New")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

        }
    }
}