package threeax.productivity.ideagen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import threeax.productivity.ideagen.ui.screens.MainScreen
import threeax.productivity.ideagen.ui.theme.IdeaGenTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            IdeaGenTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var navController = rememberNavController()
                    MainScreen(
                        context = this@MainActivity,
                        navController = navController
                    )


                    /* OLD CODE
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") { HomeScreen(
                            this@MainActivity,
                            navController
                        ) }
                        composable("feed") { FeedScreen(
                            this@MainActivity,
                            navController
                        ) }
                        composable("archive") {
                            Text("Not here yet :3")
                        }
                        // Add more destinations similarly.
                    }
                    */
                }
            }
        }
    }
}