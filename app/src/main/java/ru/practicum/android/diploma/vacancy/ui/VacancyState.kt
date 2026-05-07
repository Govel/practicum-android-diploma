package ru.practicum.android.diploma.vacancy.ui

import androidx.compose.runtime.Immutable
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetail


@Immutable
sealed interface VacancyState {

    @Immutable
    data class Content(val vacancy: VacancyDetail?) : VacancyState

    @Immutable
    object Error : VacancyState

    @Immutable
    object Empty : VacancyState

    @Immutable
    object Loading : VacancyState
}
