package threeax.productivity.ideagen.core

import Generator
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import kotlin.math.roundToInt


fun GenerateQOTD(context: Context, settings: Settings): QOTDData {
    val generator = Generator(context, settings)
    return generator.getQOTD()
}

fun GenerateMOTD(context: Context, settings: Settings): MOTDData {
    val generator = Generator(context, settings)
    return generator.getMOTD()
}

fun getCompletionRatio(rerolls: Int, completions: Int): Float {
    if (rerolls == 0 && completions > 0) {
        return 1f
    }
    if (rerolls == 0 || completions == 0) {
        return 0f
    }
    return (completions.toFloat() / rerolls.toFloat())
}

fun getCompletionStatMessage(completionRatio: Float): String {
    val ratio = (completionRatio*100).roundToInt()
    if (ratio == 0) {
        return "The journey of a thousand miles begins with a single step... or tap!"
    } else if (ratio < 25) {
        return "Hey, at least you've started! Keep going!"
    } else if (ratio < 50) {
        return "Don't give up now! You're making progress!"
    } else if (ratio < 75) {
        return "So close you can almost taste it! (Unless it's something that doesn't taste good...)"
    } else if (ratio < 100) {
        return "Can you feel the victory? It's so close!"
    } else {
        return "Perfection! You're a completionist extraordinaire!"
    }
}

@Composable
fun openFeedbackLink() {
    val uri = LocalUriHandler.current
    val url = "https://github.com/AlbertDaYoungYT/IdeaGen/issues"
    uri.openUri(url)
}