package threeax.productivity.ideagen.ui.components

import android.content.Context
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun AboutComponent(
    context: Context
) {
    val appVersion = context.packageManager.getPackageInfo(context.packageName, 0).versionName
    val androidVersion = Build.VERSION.RELEASE + " (" + Build.VERSION.SDK_INT + ")_" + Build.VERSION.CODENAME

    val creatorText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
            append("Created by ")
        }

        pushStringAnnotation(tag = "Albert Knudsen", annotation = "https://github.com/AlbertDaYoungYT")
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append("Albert Knudsen")
        }

        pop()
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
            append("\n        in collaboration with ")
        }

        pushStringAnnotation(tag = "Flames", annotation = "https://github.com/Flames08")
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append("Flames")
        }
        pop()
    }
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(modifier = Modifier.padding(top = 16.dp)) {
            Column {
                Text(
                    text = "IdeaGen: $appVersion",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Android version: $androidVersion",
                    style = MaterialTheme.typography.bodySmall
                )
                ClickableText(
                    text = creatorText,
                    style = MaterialTheme.typography.bodySmall,
                    onClick = { offset ->
                        creatorText.getStringAnnotations(tag = "Albert Knudsen", start = offset, end = offset).firstOrNull()?.let {
                            //Log.d("URL", it.item)
                            uriHandler.openUri(it.item)
                        }

                        creatorText.getStringAnnotations(tag = "Flames", start = offset, end = offset).firstOrNull()?.let {
                            //Log.d("URL", it.item)
                            uriHandler.openUri(it.item)
                        }
                    }
                )
            }
        }
    }
}