package ru.practicum.android.diploma.root.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.ui.navigation.BottomNavigationBar
import ru.practicum.android.diploma.ui.navigation.ProjectNavHost
import ru.practicum.android.diploma.ui.navigation.ScreenNavItem
import ru.practicum.android.diploma.ui.theme.AppTheme

class RootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route?.substringBefore("?") ?: ScreenNavItem.Main.route

            val bottomRoutes = listOf(
                ScreenNavItem.Main.route,
                ScreenNavItem.Favorites.route,
                ScreenNavItem.Team.route
            )

            val currentScreen = when(currentRoute){
//                ScreenNavItem.Main.route -> ScreenNavItem.Main
                ScreenNavItem.Favorites.route ->  ScreenNavItem.Favorites
                ScreenNavItem.Team.route -> ScreenNavItem.Team
                else -> ScreenNavItem.Main
            }

            AppTheme {
                Scaffold(
                    bottomBar = {
                        if (currentRoute in bottomRoutes) {
                            BottomNavigationBar(
                                navController = navController,
                                currentRoute = currentScreen
                            )
                        }
                    }
                ) { innerPadding ->
                    ProjectNavHost(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        // Пример использования access token для HeadHunter API
        networkRequestExample(accessToken = BuildConfig.API_ACCESS_TOKEN)
    }

    private fun networkRequestExample(accessToken: String) {
        // ...
    }
}
