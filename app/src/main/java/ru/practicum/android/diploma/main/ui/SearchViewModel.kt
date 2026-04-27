package ru.practicum.android.diploma.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.main.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.main.domain.models.VacancyCard
import ru.practicum.android.diploma.main.domain.models.VacancyFilter
import ru.practicum.android.diploma.main.ui.screen.ErrorHandler

class SearchViewModel(
    private val interactor: VacanciesInteractor
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private var searchJob: Job? = null
    private var currentQuery: String = ""

    fun onSearchTextChanged(text: String) {
        _state.update { it.copy(searchText = text) }
        if (text.isNotEmpty()) {
            searchDebounced(text)
        } else {
            clearSearch()
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        currentQuery = ""
        _state.update {
            it.copy(
                vacancies = emptyList(),
                isLoading = false,
                isEmptyResult = false,
                totalCount = 0,
                currentPage = 0,
                hasMorePages = true
            )
        }
    }

    private fun searchDebounced(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            if (query == _state.value.searchText) {
                performSearch(query, isNewSearch = true)
            }
        }
    }

    fun loadNextPage() {
        val currentState = _state.value
        if (!currentState.hasMorePages || currentState.isLoading) return

        val nextPage = currentState.currentPage + 1
        performSearch(currentQuery, isNewSearch = false, page = nextPage)
    }

    private fun performSearch(query: String, isNewSearch: Boolean = true, page: Int = 0) {
        if (isNewSearch) {
            currentQuery = query
            _state.update {
                it.copy(
                    isLoading = true,
                    isEmptyResult = false,
                    vacancies = emptyList(),
                    currentPage = 0,
                    hasMorePages = true
                )
            }
        } else {
            _state.update { it.copy(isLoading = true) }
        }

        val filter = VacancyFilter(
            text = query,
            page = page
        )

        viewModelScope.launch {
            interactor.searchVacancies(filter)
                .catch { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isNetworkError = true
                        )
                    }
                }
                .collect { result ->
                    handleSearchResult(result, isNewSearch, page)
                }
        }
    }

    private fun handleSearchResult(
        result: Pair<List<VacancyCard>?, String?>,
        isNewSearch: Boolean,
        page: Int
    ) {
        val (vacancies, error) = result

        if (error != null && error != "Empty") {
            val isNetworkError = ErrorHandler.getErrorType(error)

            _state.update {
                it.copy(
                    isLoading = false,
                    isNetworkError = isNetworkError,
                    isServerError = !isNetworkError,
                    isEmptyResult = false
                )
            }
            return
        }

        val vacancyList = vacancies ?: emptyList()
        val hasMore = vacancyList.size == PAGE_SIZE

        if (isNewSearch) {
            _state.update {
                it.copy(
                    isLoading = false,
                    vacancies = vacancyList,
                    totalCount = vacancyList.size,
                    isEmptyResult = vacancyList.isEmpty() || error == "Empty",
                    isNetworkError = false,
                    isServerError = false,
                    currentPage = page,
                    hasMorePages = hasMore
                )
            }
        } else {
            val currentList = _state.value.vacancies.toMutableList()
            currentList.addAll(vacancyList)
            _state.update {
                it.copy(
                    isLoading = false,
                    vacancies = currentList,
                    totalCount = currentList.size,
                    currentPage = page,
                    hasMorePages = hasMore
                )
            }
        }
    }

    fun onVacancyClick(vacancyId: String) {
        _state.update { it.copy() }

    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val PAGE_SIZE = 20
    }
}
