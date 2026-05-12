package ru.practicum.android.diploma.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.filter.industry.ui.IndustryScreen
import ru.practicum.android.diploma.filter.ui.screen.FilterScreen
import ru.practicum.android.diploma.filter.workplace.ui.WorkPlaceViewModel
import ru.practicum.android.diploma.main.ui.screen.SearchScreen
import ru.practicum.android.diploma.ui.screens.favorites.FavoritesScreen
import ru.practicum.android.diploma.ui.screens.filter.workplace.CountryScreen
import ru.practicum.android.diploma.ui.screens.filter.workplace.RegionScreen
import ru.practicum.android.diploma.ui.screens.filter.workplace.WorkPlaceScreen
import ru.practicum.android.diploma.ui.screens.team.TeamScreen
import ru.practicum.android.diploma.vacancy.ui.VacancyDetailScreen

private const val DEFAULT_SCREEN_ID = "workplace_main"

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
                    navController.navigate("${Routes.VACANCY}/$vacancyId?fromFavorites=false")
                },
                navController = navController
            )
        }

        composable(Routes.FAVORITES) {
            FavoritesScreen(
                onVacancyClick = { vacancyId, fromFavorites ->
                    navController.navigate("${Routes.VACANCY}/$vacancyId?fromFavorites=$fromFavorites")
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

        composable(Routes.WORKPLACE) { backStackEntry ->
            val screenId = backStackEntry.arguments?.getString("screenId") ?: "workplace_main"

            WorkPlaceScreen(
                onBack = { navController.popBackStack() },
                onCountry = {
                    navController.navigate(Routes.COUNTRY + "?returnTo=$screenId")
                },
                onRegion = {
                    navController.navigate(Routes.REGION)
                },
                onApply = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("filtersChanged", true)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "${Routes.VACANCY}/{vacancyId}?fromFavorites={fromFavorites}",
            arguments = listOf(
                navArgument("vacancyId") { type = NavType.StringType },
                navArgument("fromFavorites") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val vacancyId = backStackEntry.arguments?.getString("vacancyId") ?: ""
            val fromFavorites = backStackEntry.arguments?.getBoolean("fromFavorites") ?: false
            VacancyDetailScreen(
                onBack = { navController.popBackStack() },
                vacancyId = vacancyId,
                fromFavorites = fromFavorites
            )
        }

        composable(
            route = "${Routes.COUNTRY}?returnTo={returnTo}",
            arguments = listOf(
                navArgument("returnTo") {
                    type = NavType.StringType
                    defaultValue = DEFAULT_SCREEN_ID
                }
            )
        ) { backStackEntry ->
            val returnTo = backStackEntry.arguments?.getString("returnTo") ?: DEFAULT_SCREEN_ID
            val workPlaceViewModel: WorkPlaceViewModel = koinViewModel()
            CountryScreen(
                onBack = {
                    navController.popBackStack()
                },
                onCountrySelected = { country ->
                    workPlaceViewModel.selectCountry(country)
                    navController.popBackStack(returnTo, false)
                }
            )
        }

        composable(Routes.REGION) {
            RegionScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
