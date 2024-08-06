package threeax.productivity.ideagen.ui.pages

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveScreen(
    context: Context,
    navController: NavController,
    activityPressed: (Int?) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

    }
}