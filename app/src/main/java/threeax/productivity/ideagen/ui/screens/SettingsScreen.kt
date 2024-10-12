package threeax.productivity.ideagen.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import threeax.productivity.ideagen.core.AppData
import threeax.productivity.ideagen.core.Settings
import threeax.productivity.ideagen.persistence.ActivityDBHandler
import threeax.productivity.ideagen.ui.components.AboutComponent
import threeax.productivity.ideagen.ui.components.SettingsButton
import threeax.productivity.ideagen.ui.components.SettingsDropdown
import threeax.productivity.ideagen.ui.components.SettingsNumber
import threeax.productivity.ideagen.ui.components.SettingsSwitch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    context: Context,
    navController: NavController,
    settings: Settings
) {
    val showClearDataConfirmationDialog = remember { mutableStateOf(false) }

    if (showClearDataConfirmationDialog.value) {
        AlertDialog(
            icon = {
                Icon(Icons.Default.Warning, contentDescription = "Warning Icon")
            },
            title = {
                Text(text = "Clear All App Data?")
            },
            text = {
                Text(text = "Are you sure you want to clear all data, this action can not be undone.")
            },
            onDismissRequest = {
                showClearDataConfirmationDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val activityDbHandler: ActivityDBHandler = ActivityDBHandler(context)
                        activityDbHandler.remakeDatabase()
                        showClearDataConfirmationDialog.value = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showClearDataConfirmationDialog.value = false
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )

    }

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
            title = {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 72.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AppData.settingsDataCategory.forEach { itCategory ->
                if (itCategory.category == "debug" && settings.getSetting("showDebugInfo") != "true") {}
                else {
                    Text(
                        text = itCategory.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    HorizontalDivider(
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                AppData.settingsData.forEach { itData ->
                    if (itCategory.category == "debug" && settings.getSetting("showDebugInfo") != "true") {}
                    else {
                        if (itData.category == itCategory.category) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                            ) {
                                itData.description?.let { it1 ->
                                    if (itData.type != "about") {
                                        Text(
                                            text = it1,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            modifier = Modifier.align(Alignment.CenterVertically)
                                        )
                                    }
                                }

                                if (itData.type == "dropdown") {
                                    itData.options?.let { it1 ->
                                        SettingsDropdown(
                                            label = itData.title,
                                            options = it1,
                                            selectedOption = itData.value,
                                            onOptionSelected = { newOption ->
                                                itData.value = newOption
                                                itData.changeCallback?.invoke(newOption)
                                                settings.saveSettings()
                                                //settings.loadSettings()
                                            }
                                        )
                                    }
                                } else if (itData.type == "switch") {
                                    SettingsSwitch(
                                        label = itData.title,
                                        checked = itData.value.toBoolean(),
                                        onCheckedChange = { newOption ->
                                            itData.value = newOption.toString()
                                            itData.changeCallback?.invoke(newOption.toString())
                                            settings.saveSettings()
                                            //settings.loadSettings()
                                        }
                                    )
                                } else if (itData.type == "string") {
                                    //TODO: Add text
                                } else if (itData.type == "number") {
                                    SettingsNumber(
                                        label = itData.title,
                                        value = itData.value.toInt(),
                                        onValueChange = { newOption ->
                                            itData.value = newOption.toString()
                                            itData.changeCallback?.invoke(newOption.toString())
                                            settings.saveSettings()
                                            //settings.loadSettings()
                                        }
                                    )
                                } else if (itData.type == "button") {
                                    SettingsButton(
                                        label = itData.title,
                                        onClick = {
                                            if (itData.key == "feedback") {
                                                val url =
                                                    "https://github.com/AlbertDaYoungYT/ActivityGeneratorApp/issues"
                                                val intent =
                                                    Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                                context.startActivity(intent)
                                            } else if (itData.key == "clearData") {
                                                showClearDataConfirmationDialog.value = true
                                            } else if (itData.key == "dumpDatabase") {
                                                val activityDbHandler: ActivityDBHandler =
                                                    ActivityDBHandler(context)
                                                var data = activityDbHandler.readActivities()
                                                if (data != null) {
                                                    for (activity in data) {
                                                        println(activity)
                                                    }
                                                }
                                            }
                                        }
                                    )
                                } else if (itData.type == "checkbox") {
                                    //TODO: Add checkbox
                                } else if (itData.type == "about") {
                                    AboutComponent(context)
                                }
                            }
                            if (itData.category != "about") {
                                HorizontalDivider(
                                    modifier = Modifier
                                        .width(300.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}