package ru.practicum.android.diploma.root.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.ui.navigation.BottomNavigationBar
import ru.practicum.android.diploma.ui.navigation.ProjectNavHost
import ru.practicum.android.diploma.ui.navigation.Routes
import ru.practicum.android.diploma.ui.theme.AppTheme

class RootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route?.substringBefore("?") ?: Routes.SEARCH

            val bottomRoutes = listOf(
                Routes.SEARCH,
                Routes.FAVORITES,
                Routes.TEAM
            )

            AppTheme {
                Scaffold(
                    bottomBar = {
                        if (currentRoute in bottomRoutes) {
                            BottomNavigationBar(
                                navController = navController,
                                currentRoute = currentRoute,
                            )
                        }
                    }
                ) { innerPadding ->
                    ProjectNavHost(
                        navController = navController,
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
