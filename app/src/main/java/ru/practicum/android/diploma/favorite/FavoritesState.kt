package ru.practicum.android.diploma.favorite

import ru.practicum.android.diploma.main.domain.models.VacancyCard

sealed interface FavoritesState {
    data class Content(val vacancyCard: List<VacancyCard>) : FavoritesState
    data object Error : FavoritesState
    data object Empty : FavoritesState
    data object Loading : FavoritesState
}
