package threeax.productivity.ideagen.pages

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import threeax.productivity.ideagen.ui.components.PrimaryActivityComponent
import threeax.productivity.ideagen.ui.components.SecondaryActivityComponent


fun onActivityClick() {

}

fun onActivityLongClick() {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    context: Context,
    navController: NavController
) {
    Column(
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
                HorizontalDivider(
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

        HorizontalDivider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Column {
            Text(
                text = "Today's Activities:",
                fontSize = 32.sp,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
            PrimaryActivityComponent(
                title = "Try to touch grass today :3",
                reroll_count = 34,
                completion_count = 0,
                component_click = { onActivityClick() },
                component_long_click = { onActivityLongClick() }
            )
            SecondaryActivityComponent(title = "Take a fat dump in the ocean", reroll_count = 0, completion_count = 50)
        }
    }
}