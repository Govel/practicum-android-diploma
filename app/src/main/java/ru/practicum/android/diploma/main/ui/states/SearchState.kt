package ru.practicum.android.diploma.main.ui.states

import ru.practicum.android.diploma.main.domain.models.VacancyCard

data class SearchState(
    val searchText: String = "",
    val vacancies: List<VacancyCard> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isNetworkError: Boolean = false,
    val isServerError: Boolean = false,
    val isEmptyResult: Boolean = false,
    val totalFound: Int = 0,
    val currentPage: Int = 0,
    val hasMorePages: Boolean = true
)
