package threeax.productivity.ideagen

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import threeax.productivity.ideagen.core.Notifiers
import threeax.productivity.ideagen.core.Settings
import threeax.productivity.ideagen.core.server.UploadActivity
import threeax.productivity.ideagen.core.server.UploadActivityResponseParser
import threeax.productivity.ideagen.core.server.UploadActivityViewModel
import threeax.productivity.ideagen.ui.screens.FullActivityScreen
import threeax.productivity.ideagen.ui.screens.MainScreen
import threeax.productivity.ideagen.ui.screens.SettingsScreen
import threeax.productivity.ideagen.ui.theme.IdeaGenTheme
import java.util.Timer
import java.util.TimerTask


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val notification = Notification.Builder(this, "test")
            .setColor(Color.Green.toArgb())
            .setColorized(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setStyle(Notification.DecoratedCustomViewStyle())

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        Notifiers(notificationManager, this)
        val settings = Settings(this)
        settings.initiate()
        settings.loadSettings()

        //TODO: Add splash screen

        // Start threads
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                UploadActivityViewModel(UploadActivity(UploadActivityResponseParser(), settings)).syncActivities(this@MainActivity)
            }
        }, 0, 10000)

        enableEdgeToEdge()

        setContent {
            IdeaGenTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            MainScreen(
                                context = this@MainActivity,
                                activityPressed = {
                                    navController.navigate("activity/" + it)
                                },
                                onSettingsClick = {
                                    navController.navigate("settings")
                                },
                                settings = settings
                            )
                        }
                        composable("settings") {
                            SettingsScreen(
                                context = this@MainActivity,
                                navController = navController,
                                settings = settings
                            )
                        }
                        composable("activity/{id}") {
                            FullActivityScreen(
                                context = this@MainActivity,
                                navController = navController,
                                id = it.arguments?.getString("id")?.toInt() ?: 0
                            )
                        }
                    }


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