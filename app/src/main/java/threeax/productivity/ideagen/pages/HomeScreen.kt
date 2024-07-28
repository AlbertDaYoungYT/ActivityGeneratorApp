package threeax.productivity.ideagen.pages

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import threeax.productivity.ideagen.core.DialogNewTask
import threeax.productivity.ideagen.core.NewTaskViewModel
import threeax.productivity.ideagen.ui.components.NavigationBarKt
import threeax.productivity.ideagen.ui.components.PrimaryActivityComponent
import threeax.productivity.ideagen.ui.components.SecondaryActivityComponent
import threeax.productivity.ideagen.ui.components.TopNavBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    context: Context
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
            Box(
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp)
                    .clip(shape = RoundedCornerShape(0.dp, 0.dp, 32.dp, 32.dp))
                    .background(MaterialTheme.colorScheme.onSecondary)
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
                    .padding(top = 2.dp, bottom = 8.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Long before time had a name...",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                    Divider(
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd,
                    ) {
                        Text(
                            text = "~ Albert Einstein",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }

            Divider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Column(
                //verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "Today's Activities:",
                    fontSize = 32.sp,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(start = 16.dp)
                )
                PrimaryActivityComponent(title = "Try to touch grass today :3", reroll_count = 34, completion_count = 0)
                SecondaryActivityComponent(title = "Take a fat dump in the ocean", reroll_count = 0, completion_count = 50)
            }
        }
    }
}