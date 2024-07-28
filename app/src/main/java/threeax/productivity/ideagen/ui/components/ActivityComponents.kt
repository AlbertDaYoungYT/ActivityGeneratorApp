package threeax.productivity.ideagen.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PrimaryActivityComponent(
    title: String,
    description: String = "",
    reroll_count: Int,
    completion_count: Int,
    component_click: () -> Unit,
    component_long_click: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .clip(shape = RoundedCornerShape(32.dp, 32.dp, 24.dp, 24.dp))
            .background(MaterialTheme.colorScheme.onPrimary)
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
                text = title,
                fontSize = 28.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 4,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
                //fontWeight = FontWeight.SemiBold
            )
            if (description != "") {
                Text(
                    text = description,
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

            TextButton(
                onClick = { /*TODO*/ }
            ) {
                Text(
                    text = "Tap for details ...",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@Composable
fun SecondaryActivityComponent(
    title: String,
    description: String = "",
    reroll_count: Int,
    completion_count: Int
) {

}