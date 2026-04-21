package ru.practicum.android.diploma.ui.navigation

import ru.practicum.android.diploma.R

sealed class ScreenNavItem(
    val route: String,
    val title: Int,
    val icon: Int
)  {
    object Main : ScreenNavItem(
        route = "main",
        title = R.string.main,
        icon = R.drawable.ic_main_24
    )

    object Favorites : ScreenNavItem(
        route = "favorites",
        title = R.string.favorites,
        icon = R.drawable.ic_favorites_on__24
    )

    object Team : ScreenNavItem(
        route = "team",
        title = R.string.team,
        icon = R.drawable.ic_team_24
    )

    companion object {
        val items = listOf(Main, Favorites, Team)
    }
}
