package ru.practicum.android.diploma.filter.industry.ui

import android.util.Log
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
import ru.practicum.android.diploma.filter.industry.domain.api.IndustryInteractor
import ru.practicum.android.diploma.filter.industry.domain.models.FilterIndustry
import ru.practicum.android.diploma.filter.industry.domain.models.IndustriesState
import ru.practicum.android.diploma.main.data.model.NetworkState

class IndustryViewModel(
    private val interactor: IndustryInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<IndustriesState>(IndustriesState.Loading)
    val state: StateFlow<IndustriesState> = _state.asStateFlow()

    private val _selectedId = MutableStateFlow<Int>(-1)
    val selectedId: StateFlow<Int> = _selectedId.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private var searchJob: Job? = null

    var currentIndustries: List<FilterIndustry>? = emptyList()

    private val _filteredIndustries = MutableStateFlow<List<FilterIndustry>?>(emptyList())
    val filteredIndustries: StateFlow<List<FilterIndustry>?> = _filteredIndustries.asStateFlow()

    private val _isSelected = MutableStateFlow(false)
    val isSelected: StateFlow<Boolean> = _isSelected.asStateFlow()

    fun selectIndustry(id: Int) {
        _selectedId.value = id
        _isSelected.value = id != -1
        Log.d("MyTag", " _selectedId.value: ${_selectedId.value}")
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
        _selectedId.value = -1
        _isSelected.value = false
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
