package ru.practicum.android.diploma.ui.navigation

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.screens.Routes

sealed class ScreenNavItem(
    val route: String,
    val title: Int,
    val icon: Int
) {
    data object Main : ScreenNavItem(
        route = Routes.SEARCH,
        title = R.string.main,
        icon = R.drawable.ic_main_24
    )

    data object Favorites : ScreenNavItem(
        route = Routes.FAVORITES,
        title = R.string.favorites,
        icon = R.drawable.ic_favorites_on__24
    )

    data object Team : ScreenNavItem(
        route = Routes.TEAM,
        title = R.string.team,
        icon = R.drawable.ic_team_24
    )

    companion object {
        val items = listOf(Main, Favorites, Team)
    }
}
