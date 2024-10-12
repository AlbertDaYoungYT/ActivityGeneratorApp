package threeax.productivity.ideagen.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import threeax.productivity.ideagen.core.Settings
import threeax.productivity.ideagen.core.obfuscate
import threeax.productivity.ideagen.persistence.ActivityModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PrimaryActivityComponent(
    id: Int,
    component_click: () -> Unit,
    component_long_click: () -> Unit,
    value: ActivityModel,
    index: Int
) {
    Box(
        modifier = Modifier
            .padding(start = 16.dp, top = 4.dp, end = 16.dp)
            .padding(bottom = 4.dp)
            .clip(shape = if (index == 0) RoundedCornerShape(32.dp, 32.dp, 8.dp, 8.dp) else RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .combinedClickable(
                onClick = {
                    component_click()
                },
                onLongClick = {
                    component_long_click()
                }
            )
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 16.dp, 0.dp)
                .sizeIn(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            Text(
                text = value.activityName,
                fontSize = 25.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 4,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
                //fontWeight = FontWeight.SemiBold
            )
            if (value.activityDescription != "") {
                Text(
                    text = value.activityDescription,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Light,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.background
            )
            Row {
                TextButton(onClick = { /*TODO*/ })
                {
                    Text(
                        text = "Rerolls: " + value.activityReRoll.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                TextButton(onClick = { /*TODO*/ })
                {
                    Text(
                        text = "Completions: " + value.activityCompletions.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            /*
            TextButton(
                onClick = { /*TODO*/ }
            ) {
                Text(
                    text = "Tap for details ...",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            */
        }
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SecondaryActivityComponent(
    id: Int,
    component_long_click: () -> Unit,
    value: ActivityModel
) {
    var target by remember { mutableStateOf(100.dp) }
    val size by animateDpAsState(target)

    var opacity by remember { mutableStateOf(0f) }
    val statButtonOpacity by animateFloatAsState(opacity)

    Box(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .padding(bottom = 4.dp, top = 4.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .combinedClickable(
                onClick = {
                    target = if (size == 100.dp) {
                        150.dp
                    } else {
                        100.dp
                    }
                    opacity = if (statButtonOpacity == 1f) {
                        0f
                    } else {
                        1f
                    }
                },
                onLongClick = {
                    component_long_click()
                }
            )
            .height(size)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 16.dp, 16.dp)
                .sizeIn(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            Text(
                text = value.activityName,
                fontSize = 19.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 4,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
                //fontWeight = FontWeight.SemiBold
            )
            if (value.activityDescription != "") {
                Text(
                    text = value.activityDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.secondary
                )
            }


            Row(
                modifier = Modifier.alpha(statButtonOpacity)
            ) {
                TextButton(
                    onClick = { /*TODO*/ },
                    enabled = statButtonOpacity == 1f
                )
                {
                    Text(
                        text = "Rerolls: " + value.activityReRoll.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                TextButton(
                    onClick = { /*TODO*/ },
                    enabled = statButtonOpacity == 1f
                )
                {
                    Text(
                        text = "Completions: " + value.activityCompletions.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            /*
            TextButton(
                onClick = { /*TODO*/ }
            ) {
                Text(
                    text = "Tap for details ...",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            */
        }
    }
}

@Composable
fun FutureActivityComponent(
    id: Int,
    component_click: () -> Unit,
    component_long_click: () -> Unit,
    settings: Settings,
    value: ActivityModel
) {
    Box(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .padding(bottom = 4.dp, top = 4.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 16.dp, 16.dp)
                .sizeIn(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            Text(
                text = if (settings.getSetting("obfuscateFutureTasks") == "true") obfuscate(value.activityName) else value.activityName,
                fontSize = 19.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 4,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
                //fontWeight = FontWeight.SemiBold
            )
            if (value.activityDescription != "") {
                Text(
                    text = if (settings.getSetting("obfuscateFutureTasks") == "true") obfuscate(value.activityDescription) else value.activityDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}