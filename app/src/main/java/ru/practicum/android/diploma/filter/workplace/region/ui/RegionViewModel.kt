package ru.practicum.android.diploma.filter.workplace.region.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.FilterSettingsInteractor
import ru.practicum.android.diploma.filter.workplace.region.domain.api.RegionFilterInteractor
import ru.practicum.android.diploma.filter.workplace.region.domain.models.FilterRegion
import ru.practicum.android.diploma.filter.workplace.region.domain.models.RegionsState

class RegionViewModel(
    private val interactor: RegionFilterInteractor,
    private val filterInteractor: FilterSettingsInteractor
) : ViewModel() {
    private val _state = MutableStateFlow<RegionsState>(RegionsState.Loading)
    val state: StateFlow<RegionsState> = _state.asStateFlow()
    private val _selectedRegion = MutableStateFlow<FilterRegion?>(null)

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private var searchJob: Job? = null
    private var currentRegions: List<FilterRegion>? = emptyList()
    private val _filteredRegions = MutableStateFlow<List<FilterRegion>?>(emptyList())

    init {
        loadRegions()
    }

    fun loadRegions() {
        viewModelScope.launch {
            _state.update { RegionsState.Loading }
            currentRegions = interactor.getRegions()
            if (currentRegions?.isEmpty() ?: false) {
                _state.update { RegionsState.Error }
            } else {
                _state.update { RegionsState.Content(currentRegions!!) }
            }
        }
    }

    fun selectRegion(region: FilterRegion) {
        _selectedRegion.update { region }
        filterInteractor.saveRegion(
            countryId = filterInteractor.getRegion().countryId,
            countryName = filterInteractor.getRegion().countryName,
            regionId = region.id,
            regionName = region.name
        )
    }

    fun onSearchTextChanged(query: String) {
        _query.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            filterRegions(query)
        }
    }

    private fun filterRegions(query: String = _query.value) {
        val searchText = query.lowercase()

        val filtered = currentRegions?.filter { it.name.lowercase().contains(searchText) }

        val currentSelection = _selectedRegion.value
        if (currentSelection != null && (filtered == null || !filtered.contains(currentSelection))) {
            _selectedRegion.value = null
        }

        _filteredRegions.value = filtered ?: emptyList()
        updateStateBasedOnRegions()
    }

    private fun updateStateBasedOnRegions() {
        val filtered = _filteredRegions.value
        if (filtered.isNullOrEmpty()) {
            _state.update { RegionsState.Empty }
        } else {
            _state.update { RegionsState.Content(filtered) }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
