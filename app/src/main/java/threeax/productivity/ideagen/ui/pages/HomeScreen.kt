package threeax.productivity.ideagen.ui.pages

import Generator
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import threeax.productivity.ideagen.core.BottomSheetStateModal
import threeax.productivity.ideagen.core.GenerateQOTD
import threeax.productivity.ideagen.core.Settings
import threeax.productivity.ideagen.persistence.ActivityDBHandler
import threeax.productivity.ideagen.ui.components.FutureActivityComponent
import threeax.productivity.ideagen.ui.components.PrimaryActivityComponent


fun onActivityClick() {

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    context: Context,
    navController: NavController,
    database: ActivityDBHandler,
    settings: Settings,
    showBottomSheet: BottomSheetStateModal,
    activityPressed: (Int?) -> Unit
) {
    val currentQOTD = GenerateQOTD(context, settings)
    val todaysActivities = Generator(context, settings).getTodaysActivities() // TODO: Make this
    val tomorrowsActivities = Generator(context, settings).getFutureActivities()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        LazyColumn {
            if (settings.getSetting("qotd") == "true") {
                item {
                    Box(
                        modifier = Modifier
                            .padding(start = 24.dp, end = 24.dp)
                            .clip(shape = RoundedCornerShape(0.dp, 0.dp, 32.dp, 32.dp))
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = {
                                    //component_click()
                                },
                                onLongClick = {
                                    val clipboard =
                                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("label", currentQOTD.text)
                                    clipboard.setPrimaryClip(clip)
                                    Toast
                                        .makeText(
                                            context,
                                            "Copied to Clipboard",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                            )
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
                                    text = currentQOTD.text,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.CenterEnd,
                            ) {
                                Text(
                                    text = "~ " + currentQOTD.from.split(",")[0],
                                    textAlign = TextAlign.Center,
                                    fontSize = 14.sp,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            }
                        }
                    }
                }
            }
            item {
                /*
                Text(
                    text = "Today's Activities:",
                    fontSize = 28.sp,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .padding(top = 16.dp)
                )*/

                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp),
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            if (todaysActivities.size == 0) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 96.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(start = 64.dp, end = 64.dp)
                                .fillMaxWidth()
                                .clip(ShapeDefaults.ExtraLarge)
                                .background(MaterialTheme.colorScheme.inverseOnSurface)
                                .padding(32.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Filled.Info,
                                    contentDescription = "Localized description",
                                    modifier = Modifier
                                        .padding(bottom = 16.dp)
                                        .size(32.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Not enough activities to show",
                                    fontSize = 22.sp,
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }
                    }
                }
            } else if (todaysActivities.size >= 1) {
                items(todaysActivities.size) {it ->
                    PrimaryActivityComponent(
                        id = todaysActivities[it].activityId,
                        component_click = {
                            activityPressed(todaysActivities[it].activityId)
                        },
                        component_long_click = {
                            showBottomSheet.showBottomSheet = true
                            showBottomSheet.activityId = todaysActivities[it].activityId
                            showBottomSheet.activityIndex = it
                        },
                        value = todaysActivities[it],
                        index = it
                    )
                }
                items(tomorrowsActivities.size) {
                    FutureActivityComponent(
                        id = tomorrowsActivities[it].activityId,
                        component_click = {
                            activityPressed(tomorrowsActivities[it].activityId)
                        },
                        component_long_click = {
                            showBottomSheet.showBottomSheet = true
                            showBottomSheet.activityId = tomorrowsActivities[it].activityId
                            showBottomSheet.activityIndex = it
                        },
                        settings = settings,
                        value = tomorrowsActivities[it]
                    )
                }
                /*if (todaysActivities.size > 1) {
                    items(2) { it ->
                        var index = it + 1
                        if (todaysActivities.size > index) {
                            SecondaryActivityComponent(
                                id = todaysActivities[index].activityId,
                                component_long_click = {
                                    showBottomSheet.showBottomSheet = true
                                    showBottomSheet.activityId = todaysActivities[index].activityId
                                    showBottomSheet.activityIndex = index
                                },
                                value = todaysActivities[index]
                            )
                        }
                    }
                }*/
            }
        }
    }
}
