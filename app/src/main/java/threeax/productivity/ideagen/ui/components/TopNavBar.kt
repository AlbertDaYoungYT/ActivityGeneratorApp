package threeax.productivity.ideagen.ui.components

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import threeax.productivity.ideagen.core.GenerateMOTD
import threeax.productivity.ideagen.core.Settings


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(
    context: Context,
    settings: Settings,
    onSettingsClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Surface(
        shadowElevation = 8.dp
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = GenerateMOTD(
                        context,
                        settings
                    ).text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        onSettingsClick()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Localized description"
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.onSecondary
            ),
            scrollBehavior = scrollBehavior,
        )
    }
}