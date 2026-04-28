package ru.practicum.android.diploma.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.practicum.android.diploma.main.ui.screen.SearchScreen
import ru.practicum.android.diploma.ui.screens.FavoritesScreen
import ru.practicum.android.diploma.ui.screens.TeamScreen
import ru.practicum.android.diploma.ui.screens.VacancyDetailScreen
import ru.practicum.android.diploma.ui.screens.filter.FilterScreen

@Composable
fun ProjectNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SEARCH,
        modifier = modifier
    ) {
        composable(Routes.SEARCH) {
            SearchScreen(
                onFilter = { navController.navigate(Routes.FILTER) },
                isFilterActive = false
            )
        }

        composable(Routes.FAVORITES) {
            FavoritesScreen()
        }

        composable(Routes.TEAM) {
            TeamScreen()
        }

        composable(Routes.FILTER) {
            FilterScreen()
        }

        composable(Routes.VACANCY) {
            VacancyDetailScreen()
        }

    }
}
