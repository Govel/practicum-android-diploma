package ru.practicum.android.diploma.favorite

import androidx.compose.runtime.Immutable
import ru.practicum.android.diploma.main.domain.models.VacancyCard


@Immutable
sealed interface FavoritesState {
    @Immutable
    data class Content(val vacancyCard: List<VacancyCard>) : FavoritesState

    @Immutable
    data object Error : FavoritesState

    @Immutable
    data object Empty : FavoritesState

    @Immutable
    data object Loading : FavoritesState
}
