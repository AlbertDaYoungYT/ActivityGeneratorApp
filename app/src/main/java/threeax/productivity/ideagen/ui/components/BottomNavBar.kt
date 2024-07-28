package threeax.productivity.ideagen.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.navOptions
import threeax.productivity.ideagen.R

@Composable
fun NavigationBarKt(
    navController: NavController
) {
    NavigationBar(
        content = {
            NavigationBarItem(
                true,
                onClick = {
                    navController.navigate(
                        "home",
                        navOptions { // Use the Kotlin DSL for building NavOptions
                            anim {
                                enter = android.R.animator.fade_in
                                exit = android.R.animator.fade_out
                            }
                        }
                    )
                },
                icon = {
                    Icon(
                        Icons.Filled.Home,
                        contentDescription = "Home Button"
                    )
                },
                label = {
                    Text(text = "Home")
                }
            )
            NavigationBarItem(false, onClick = {
                navController.navigate(
                    "feed",
                    navOptions { // Use the Kotlin DSL for building NavOptions
                        anim {
                            enter = android.R.animator.fade_in
                            exit = android.R.animator.fade_out
                        }
                    })
            },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.newspaper_24px),
                        contentDescription = "Feed Button"
                    )
                },
                label = {
                    Text(text = "Feed")
                }
            )
            NavigationBarItem(false, onClick = {
                navController.navigate("archive")
            },
                icon = {
                    Icon(
                        Icons.AutoMirrored.Filled.List,
                        contentDescription = "Archive Button"
                    )
                },
                label = {
                    Text(text = "Archive")
                }
            )
        }
    )
}