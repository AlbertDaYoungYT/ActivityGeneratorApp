package threeax.productivity.ideagen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import threeax.productivity.ideagen.pages.FeedScreen
import threeax.productivity.ideagen.pages.HomeScreen
import threeax.productivity.ideagen.ui.theme.IdeaGenTheme

@Serializable
object Home
@Serializable
object Feed


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IdeaGenTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "Home") {
                        composable("Home") { HomeScreen(this@MainActivity) }
                        composable("Feed") { FeedScreen(this@MainActivity) }
                        // Add more destinations similarly.
                    }
                }
            }
        }
    }
}