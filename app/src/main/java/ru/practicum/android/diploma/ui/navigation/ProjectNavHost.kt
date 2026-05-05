package ru.practicum.android.diploma.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.practicum.android.diploma.filter.industry.ui.IndustryScreen
import ru.practicum.android.diploma.main.ui.screen.SearchScreen
import ru.practicum.android.diploma.ui.screens.favorites.FavoritesScreen
import ru.practicum.android.diploma.filter.ui.screen.FilterScreen
import ru.practicum.android.diploma.ui.screens.filter.workplace.WorkPlaceScreen
import ru.practicum.android.diploma.ui.screens.team.TeamScreen
import ru.practicum.android.diploma.vacancy.ui.VacancyDetailScreen

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
                onVacancyClick = { vacancyId ->
                    navController.navigate("${Routes.VACANCY}/$vacancyId")
                },
                navController = navController
            )
        }

        composable(Routes.FAVORITES) {
            FavoritesScreen(
                onVacancyClick = { vacancyId ->
                    navController.navigate("${Routes.VACANCY}/$vacancyId")
                }
            )
        }

        composable(Routes.TEAM) {
            TeamScreen()
        }

        composable(Routes.FILTER) {
            FilterScreen(
                onBack = { navController.popBackStack() },
                onApply = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("filtersChanged", true)
                    navController.popBackStack()
                },
                onWorkPlace = { navController.navigate(Routes.WORKPLACE) },
                onIndustry = { navController.navigate(Routes.INDUSTRY) },
            )
        }

        composable(Routes.INDUSTRY) {
            IndustryScreen(
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.WORKPLACE) {
            WorkPlaceScreen()
        }

        composable(
            route = "${Routes.VACANCY}/{vacancyId}",
            arguments = listOf(navArgument("vacancyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val vacancyId = backStackEntry.arguments?.getString("vacancyId") ?: ""
            VacancyDetailScreen(
                onBack = { navController.popBackStack() },
                vacancyId = vacancyId
            )
        }
    }
}
