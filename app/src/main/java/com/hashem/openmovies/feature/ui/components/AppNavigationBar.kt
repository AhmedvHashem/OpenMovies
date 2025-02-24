package com.hashem.openmovies.feature.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.hashem.openmovies.AppRoute

@Composable
fun AppNavigationBar(
    navController: NavController,
    currentRoute: String?,
    routes: List<AppRoute.Movies>,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) {
        routes.forEach { appRoute ->
            NavigationBarItem(
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        contentDescription = appRoute.label,
                        imageVector = appRoute.icon,
                    )
                },
                label = {
                    Text(
                        text = appRoute.label,
                        style = MaterialTheme.typography.bodySmall,
                    )
                },
                selected = currentRoute?.contains(appRoute.route) == true,
                onClick = {
                    navController.navigate(appRoute.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}