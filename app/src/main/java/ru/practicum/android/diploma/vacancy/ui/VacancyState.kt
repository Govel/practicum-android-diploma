package ru.practicum.android.diploma.vacancy.ui

import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetail

sealed interface VacancyState {
    data class Content(val vacancy: VacancyDetail) : VacancyState
    object Error : VacancyState
    object Empty : VacancyState
    object Loading : VacancyState
}
