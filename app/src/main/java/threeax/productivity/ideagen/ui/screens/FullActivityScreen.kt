package threeax.productivity.ideagen.ui.screens

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.MutableStateFlow
import threeax.productivity.ideagen.core.completionRatioViewModel
import threeax.productivity.ideagen.core.getCompletionRatio
import threeax.productivity.ideagen.core.getCompletionStatMessage
import threeax.productivity.ideagen.persistence.ActivityDBHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullActivityScreen(
    context: Context = LocalContext.current,
    navController: NavController = rememberNavController(),
    database: ActivityDBHandler = ActivityDBHandler(context),
    id: Int = 0,
) {
    var activity by remember { mutableStateOf(database.readActivity(id)) }
    val ratioViewModel = remember { completionRatioViewModel() }
    val completionRatio = remember { MutableStateFlow(getCompletionRatio(activity.activityReRoll, activity.activityCompletions)) }
    ratioViewModel.completionRatio = completionRatio.collectAsState().value

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                //containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                navigationIconContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                actionIconContentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),

            modifier = Modifier.shadow(elevation = 4.dp),
            navigationIcon = {
                IconButton(
                    onClick = {
                        navController.navigateUp()
                    }
                ) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, "Back")
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        //TODO: Edit Activity
                        //navController.navigate("edit_activity")
                    }
                ) {
                    Icon(Icons.Filled.Create, "Edit")
                }
            },
            title = {
                Text(
                    text = activity.activityName,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 10.dp),
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 128.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = activity.activityName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = activity.activityDescription,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Box(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(32.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(16.dp),
            ) {
                Column {
                    val ratio by completionRatio.collectAsState()
                    Text(
                        text = getCompletionStatMessage(ratio),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    SuggestionChip(
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .align(alignment = Alignment.CenterHorizontally),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                        onClick = { /*TODO*/ },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Localized description"
                            )
                        },
                        label = { Text((ratio * 100).toInt().toString() + "% C/R Ratio") }
                    )
                    Row {
                        OutlinedButton(
                            onClick = {
                                database.rerollActivity(id)
                                activity = database.readActivity(id)
                                completionRatio.value = getCompletionRatio(activity.activityReRoll, activity.activityCompletions)
                                // TODO: Add to todays rerolls
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        ) {
                            Text(
                                text = "Reroll",
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                        Button(
                            onClick = {
                                database.completeActivity(id)
                                activity = database.readActivity(id)
                                completionRatio.value = getCompletionRatio(activity.activityReRoll, activity.activityCompletions)
                                // TODO: Add to todays completions
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            ),
                        ) {
                            Text(
                                text = "Complete",
                                color = MaterialTheme.colorScheme.tertiaryContainer
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(32.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(16.dp)
            ) {

            }
        }
    }
}