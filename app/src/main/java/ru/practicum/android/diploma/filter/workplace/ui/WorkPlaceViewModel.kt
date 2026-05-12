package ru.practicum.android.diploma.filter.workplace.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.FilterSettingsInteractor
import ru.practicum.android.diploma.filter.workplace.country.domain.models.Country
import ru.practicum.android.diploma.filter.workplace.region.domain.models.FilterRegion

class WorkPlaceViewModel(
    private val filterInteractor: FilterSettingsInteractor
) : ViewModel() {

    private val _selectedCountry = MutableStateFlow<Country?>(null)
    val selectedCountry: StateFlow<Country?> = _selectedCountry.asStateFlow()

    private val _selectedRegion = MutableStateFlow<FilterRegion?>(null)
    val selectedRegion: StateFlow<FilterRegion?> = _selectedRegion.asStateFlow()

    init {
        loadSavedData()
    }

    private fun loadSavedData() {
        viewModelScope.launch {
            val saved = filterInteractor.getRegion()
            if (saved.countryId > 0) {
                _selectedCountry.update { Country(saved.countryId, saved.countryName) }
            }
            if (saved.regionId > 0) {
                _selectedRegion.update { FilterRegion(saved.regionId, saved.regionName) }
            }
        }
    }

    fun selectCountry(country: Country) {
        _selectedCountry.update { country }
        _selectedRegion.update { null }
        saveToPrefs(countryId = country.id, countryName = country.name, regionId = -1, regionName = "")
    }

    fun selectRegion(region: FilterRegion) {
        _selectedRegion.update { region }
        val currentCountry = _selectedCountry.value
        saveToPrefs(
            countryId = currentCountry?.id ?: filterInteractor.getRegion().countryId,
            countryName = currentCountry?.name ?: filterInteractor.getRegion().countryName,
            regionId = region.id,
            regionName = region.name
        )
    }

    fun clearCountry() {
        _selectedCountry.update { null }
        _selectedRegion.update { null }
        saveToPrefs(countryId = -1, countryName = "", regionId = -1, regionName = "")
    }

    fun clearRegion() {
        _selectedRegion.update { null }
        val currentCountry = _selectedCountry.value
        saveToPrefs(
            countryId = currentCountry?.id ?: filterInteractor.getRegion().countryId,
            countryName = currentCountry?.name ?: filterInteractor.getRegion().countryName,
            regionId = -1,
            regionName = ""
        )
    }

    private fun saveToPrefs(countryId: Int, countryName: String, regionId: Int, regionName: String) {
        viewModelScope.launch {
            filterInteractor.saveRegion(countryId, countryName, regionId, regionName)
        }
    }
}
