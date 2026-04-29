package ru.practicum.android.diploma.main.ui

import android.content.Context
import android.widget.Toast
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
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.main.data.model.VacanciesSearchState
import ru.practicum.android.diploma.main.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.main.domain.models.VacancyCard
import ru.practicum.android.diploma.main.domain.models.VacancyFilter
import ru.practicum.android.diploma.main.ui.states.ErrorHandler
import ru.practicum.android.diploma.main.ui.states.SearchState

class SearchViewModel(
    private val interactor: VacanciesInteractor,
    private val context: Context
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
                isLoadingMore = false,
                isEmptyResult = false,
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
        if (!currentState.hasMorePages || currentState.isLoading || currentState.isLoadingMore) return

        val nextPage = currentState.currentPage + 1
        performSearch(currentQuery, isNewSearch = false, page = nextPage)
    }

    private fun showPaginationErrorToast() {
        Toast.makeText(
            context,
            context.getString(R.string.check_internet),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun performSearch(query: String, isNewSearch: Boolean = true, page: Int = 0) {
        if (isNewSearch) {
            currentQuery = query
            _state.update {
                it.copy(
                    isLoading = true,
                    isLoadingMore = false,
                    isEmptyResult = false,
                    vacancies = emptyList(),
                    currentPage = 0,
                    hasMorePages = true
                )
            }
        } else {
            _state.update { it.copy(isLoadingMore = true) }
        }
        val filter = VacancyFilter(
            text = query,
            page = page
        )
        viewModelScope.launch {
            interactor.searchVacancies(filter)
                .catch { e ->
                    if (isNewSearch) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isLoadingMore = false,
                                isNetworkError = true
                            )
                        }
                    } else {
                        _state.update { it.copy(isLoadingMore = false) }
                        showPaginationErrorToast()
                    }
                }
                .collect { result ->
                    handleSearchResult(result, isNewSearch, page)
                }
        }
    }

    private fun handleSearchResult(
        result: Pair<List<VacancyCard>?, Pair<Int?, String?>?>,
        isNewSearch: Boolean,
        page: Int
    ) {
        val (vacancies, meta) = result
        val totalFound = meta?.first
        val error = meta?.second
        if (error != null && error != VacanciesSearchState.Empty.state) {
            val isNetworkError = ErrorHandler.getErrorType(error)
            if (isNewSearch) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        isNetworkError = isNetworkError,
                        isServerError = !isNetworkError,
                        isEmptyResult = false
                    )
                }
            } else {
                _state.update { it.copy(isLoadingMore = false) }
                showPaginationErrorToast()
            }
            return
        }

        val vacancyList = vacancies ?: emptyList()
        val hasMore = vacancyList.size == PAGE_SIZE
        if (isNewSearch) {
            copyVacancies(
                vacancyList = vacancyList,
                page = page,
                hasMore = hasMore,
                error = error,
                totalFound = totalFound ?: vacancyList.size
            )
        } else {
            addAllVacancies(
                vacancyList = vacancyList,
                page = page,
                hasMore = hasMore
            )
        }
    }

    private fun copyVacancies(
        vacancyList: List<VacancyCard>,
        page: Int,
        hasMore: Boolean,
        error: String?,
        totalFound: Int
    ) {
        _state.update {
            it.copy(
                isLoading = false,
                isLoadingMore = false,
                vacancies = vacancyList,
                totalFound = totalFound,
                isEmptyResult = vacancyList.isEmpty() || error == VacanciesSearchState.Empty.state,
                isNetworkError = false,
                isServerError = false,
                currentPage = page,
                hasMorePages = hasMore
            )
        }
    }

    private fun addAllVacancies(
        vacancyList: List<VacancyCard>,
        page: Int,
        hasMore: Boolean
    ) {
        val currentList = _state.value.vacancies.toMutableList()
        currentList.addAll(vacancyList)
        _state.update {
            it.copy(
                isLoading = false,
                isLoadingMore = false,
                vacancies = currentList,
                currentPage = page,
                hasMorePages = hasMore
            )
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val PAGE_SIZE = 20
    }
}
