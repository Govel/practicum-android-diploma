@file:Suppress("MagicNumber", "LargeClass")

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
import ru.practicum.android.diploma.filter.domain.api.FilterSettingsInteractor
import ru.practicum.android.diploma.main.data.model.NetworkState
import ru.practicum.android.diploma.main.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.main.domain.models.VacancyCard
import ru.practicum.android.diploma.main.domain.models.VacancyFilter
import ru.practicum.android.diploma.main.ui.states.ErrorType
import ru.practicum.android.diploma.main.ui.states.SearchState

class SearchViewModel(
    private val interactor: VacanciesInteractor,
    private val filterSettingsInteractor: FilterSettingsInteractor,
    private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val _filterIsActive = MutableStateFlow(false)
    val filterIsActive: StateFlow<Boolean> = _filterIsActive.asStateFlow()

    private var searchJob: Job? = null
    private var currentQuery: String = ""

    init {
        viewModelScope.launch {
            collectFilterState()
        }
    }

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

    private suspend fun collectFilterState() {
        while (true) {
            delay(1000)
            _filterIsActive.update { filterSettingsInteractor.hasAnyFilter() }
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

    fun refreshSearch() {
        val currentSearchText = _state.value.searchText
        if (currentSearchText.isNotEmpty()) {
            searchJob?.cancel()
            performSearch(currentSearchText, isNewSearch = true)
        }
    }

    private fun performSearch(query: String, isNewSearch: Boolean = true, page: Int = 0) {
        if (isNewSearch) {
            startNewSearch(query)
        } else {
            startPagination()
        }

        val salary = filterSettingsInteractor.getSalary()
        val onlyWithSalary = filterSettingsInteractor.getOnlyWithSalary()
        val industryId = filterSettingsInteractor.getIndustryId()

        val filter = VacancyFilter(
            text = query,
            page = page,
            salary = salary.toIntOrNull(),
            onlyWithSalary = if (onlyWithSalary) true else null,
            industry = if (industryId != -1) industryId else null
        )

        viewModelScope.launch {
            interactor.searchVacancies(filter)
                .catch { e -> handleSearchError(e, isNewSearch) }
                .collect { result -> handleSearchResult(result, isNewSearch, page) }
        }
    }

    private fun startNewSearch(query: String) {
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
    }

    private fun startPagination() {
        _state.update { it.copy(isLoadingMore = true) }
    }

    private fun handleSearchError(throwable: Throwable, isNewSearch: Boolean) {
        val errorType = when (throwable) {
            is java.net.UnknownHostException,
            is java.net.SocketTimeoutException,
            is java.io.IOException -> ErrorType.Network
            else -> ErrorType.Unknown
        }
        updateErrorState(errorType, isNewSearch)
    }

    private fun handleSearchResult(
        result: Pair<List<VacancyCard>?, Pair<Int?, String?>?>,
        isNewSearch: Boolean,
        page: Int
    ) {
        val (vacancies, meta) = result
        val totalFound = meta?.first
        val error = meta?.second

        if (error != null && error != NetworkState.Empty.state) {
            handleSearchErrorState(error, isNewSearch)
            return
        }

        processSuccessResult(vacancies, totalFound, isNewSearch, page)
    }

    private fun handleSearchErrorState(error: String, isNewSearch: Boolean) {
        val errorType = mapToErrorType(error)
        updateErrorState(errorType, isNewSearch)
    }

    private fun updateErrorState(errorType: ErrorType, isNewSearch: Boolean) {
        if (isNewSearch) {
            _state.update {
                it.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    errorType = errorType,
                    isEmptyResult = false
                )
            }
        } else {
            _state.update { it.copy(isLoadingMore = false) }
            showPaginationErrorToast()
        }
    }

    private fun processSuccessResult(
        vacancies: List<VacancyCard>?,
        totalFound: Int?,
        isNewSearch: Boolean,
        page: Int
    ) {
        val vacancyList = vacancies ?: emptyList()
        val hasMore = vacancyList.size == PAGE_SIZE

        if (isNewSearch) {
            copyVacancies(
                vacancyList = vacancyList,
                page = page,
                hasMore = hasMore,
                error = null,
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
                isEmptyResult = vacancyList.isEmpty() || error == NetworkState.Empty.state,
                errorType = null,
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

    private fun mapToErrorType(rawError: String): ErrorType {
        return when {
            rawError == NetworkState.NoConnection.state || rawError == "no_connection" -> ErrorType.Network
            rawError == NetworkState.Error.state || rawError.startsWith("5") -> ErrorType.Server
            else -> ErrorType.Unknown
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val PAGE_SIZE = 20
    }
}
