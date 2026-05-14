package ru.practicum.android.diploma.main.ui.states

import androidx.compose.runtime.Immutable
import ru.practicum.android.diploma.main.domain.models.VacancyCard

@Immutable
data class SearchState(
    val searchText: String = "",
    val vacancies: List<VacancyCard> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorType: ErrorType? = null,
    val isServerError: Boolean = false,
    val isEmptyResult: Boolean = false,
    val totalFound: Int = 0,
    val currentPage: Int = 0,
    val hasMorePages: Boolean = true
)
