package ru.practicum.android.diploma.filter.industry.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.FilterSettingsInteractor
import ru.practicum.android.diploma.filter.industry.domain.api.IndustryInteractor
import ru.practicum.android.diploma.filter.industry.domain.models.FilterIndustry
import ru.practicum.android.diploma.filter.industry.domain.models.IndustriesState
import ru.practicum.android.diploma.main.data.model.NetworkState

class IndustryViewModel(
    private val interactor: IndustryInteractor,
    private val filterSettingsInteractor: FilterSettingsInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<IndustriesState>(IndustriesState.Loading)
    val state: StateFlow<IndustriesState> = _state.asStateFlow()

    private val _selectedIndustry = MutableStateFlow<FilterIndustry?>(null)
    val selectedIndustry: StateFlow<FilterIndustry?> = _selectedIndustry.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private var searchJob: Job? = null
    private var currentIndustries: List<FilterIndustry>? = emptyList()
    private val _filteredIndustries = MutableStateFlow<List<FilterIndustry>?>(emptyList())

    val isSelected: StateFlow<Boolean> = selectedIndustry.map { it != null }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT),
        initialValue = false
    )

    fun selectIndustry(industry: FilterIndustry) {
        _selectedIndustry.value = industry
        Log.d("MyTag", " _selectedId.value: ${industry.name}")
    }

    fun loadIndustries() {
        viewModelScope.launch {
            interactor.getIndustries()
                .catch {
                    _state.update {
                        IndustriesState.Error
                    }
                }
                .collect { result ->
                    handleIndustriesResult(result)
                }
        }
    }

    private fun handleIndustriesResult(result: Pair<List<FilterIndustry>?, String?>) {
        val (industries, error) = result
        if (error != null && error != NetworkState.Empty.state) {
            _state.update { IndustriesState.Error }
            return
        }
        currentIndustries = industries ?: emptyList()
        filterIndustries(_query.value)
    }

    fun onSearchTextChanged(query: String) {
        _query.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            filterIndustries(query)
        }
    }

    private fun filterIndustries(query: String = _query.value) {
        val searchText = query.lowercase()
        val filtered = currentIndustries?.filter { it.name.lowercase().contains(searchText) }

        val currentSelection = _selectedIndustry.value
        if (currentSelection != null && (filtered == null || !filtered.contains(currentSelection))) {
            _selectedIndustry.value = null
        }

        _filteredIndustries.value = filtered ?: emptyList()
        updateStateBasedOnFiltered()
    }

    private fun updateStateBasedOnFiltered() {
        val filtered = _filteredIndustries.value
        if (filtered.isNullOrEmpty()) {
            _state.update { IndustriesState.Empty }
        } else {
            _state.update { IndustriesState.Content(filtered) }
        }
    }

    fun resetSelection() {
        _selectedIndustry.value = null
    }

    fun saveIndustry(industry: FilterIndustry?) {
        industry?.let {
            filterSettingsInteractor.saveIndustry(it.id, it.name)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val STOP_TIMEOUT = 5000L
    }
}
