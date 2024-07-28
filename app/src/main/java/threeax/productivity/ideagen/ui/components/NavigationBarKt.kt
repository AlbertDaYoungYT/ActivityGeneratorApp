package threeax.productivity.ideagen.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import threeax.productivity.ideagen.R

@Composable
fun NavigationBarKt() {
    NavigationBar(
        content = {
            NavigationBarItem(true, onClick = { /* do something */ },
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
            NavigationBarItem(false, onClick = { /* do something */ },
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
            NavigationBarItem(false, onClick = { /* do something */ },
                icon = {
                    Icon(
                        Icons.Filled.List,
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