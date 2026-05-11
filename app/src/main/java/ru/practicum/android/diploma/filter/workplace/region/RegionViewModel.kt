@file:Suppress("MagicNumber")

package ru.practicum.android.diploma.filter.workplace.region

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.workplace.region.domain.models.FilterRegion
import ru.practicum.android.diploma.filter.workplace.region.domain.models.RegionsState

class RegionViewModel : ViewModel() {
    private val _state = MutableStateFlow<RegionsState>(RegionsState.Loading)
    val state: StateFlow<RegionsState> = _state.asStateFlow()

    private val _selectedRegion = MutableStateFlow<FilterRegion?>(null)
    val selectedRegion: StateFlow<FilterRegion?> = _selectedRegion.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private var searchJob: Job? = null
    private var currentRegions: List<FilterRegion>? = emptyList()
    private val _filteredRegions = MutableStateFlow<List<FilterRegion>?>(emptyList())

    fun selectRegion(region: FilterRegion) {
        _selectedRegion.value = region
        Log.d("MyTag", " _selectedId.value: ${region.name}")
    }

    fun loadRegiones() {
        val regions = getRegions()
        currentRegions = regions ?: emptyList()
        filterRegions(_query.value)
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

    fun resetSelection() {
        _selectedRegion.value = null
    }

    fun saveRegion(region: FilterRegion?) {
        region?.let {
            // interactor.saveRegion(it.id, it.name)
        }
    }

    private fun getRegions(): List<FilterRegion> {
        return listOf(
            FilterRegion(1, "Москва"),
            FilterRegion(2, "Апрелевка"),
            FilterRegion(3, "Балашиха"),
            FilterRegion(4, "Бронницы"),
            FilterRegion(5, "Верея"),
            FilterRegion(6, "Видное"),
            FilterRegion(7, "Волоколамск"),
            FilterRegion(8, "Воскресенск"),
            FilterRegion(9, "Высоковск"),
            FilterRegion(10, "Голицино")
        )
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val STOP_TIMEOUT = 5000L
    }
}
