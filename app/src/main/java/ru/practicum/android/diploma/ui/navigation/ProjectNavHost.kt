package ru.practicum.android.diploma.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.practicum.android.diploma.ui.screens.FavoritesScreen
import ru.practicum.android.diploma.ui.screens.SearchScreen
import ru.practicum.android.diploma.ui.screens.TeamScreen
import ru.practicum.android.diploma.ui.screens.VacancyDetailScreen
import ru.practicum.android.diploma.ui.screens.filter.FilterScreen

@Composable
fun ProjectNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
        NavHost(
            navController = navController,
            startDestination = "search_screen",
            modifier = modifier
        ){
            composable("search_screen") {
                SearchScreen(
                    onFilter = {navController.navigate("filter_screen")},
                    onVacancies = {navController.navigate("vacancy_screen")}
                )
            }

            composable("favorites_screen") {
                FavoritesScreen()
            }

            composable("team_screen") {
                TeamScreen()
            }

            composable("filter_screen") {
                FilterScreen()
            }

            composable("vacancy_screen") {
                VacancyDetailScreen()
            }

        }
}
