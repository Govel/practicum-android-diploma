package ru.practicum.android.diploma.filter.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.FilterSettingsInteractor

@Immutable
data class FilterUiState(
    val salary: String = "",
    val onlyWithSalary: Boolean = false,
    val hasAnyFilter: Boolean = false,
    val industry: String = "",
    val industryId: Int = -1,
    val region: String = ""
)

class FilterViewModel(
    private val filterSettingsInteractor: FilterSettingsInteractor
) : ViewModel() {

    private val _state = MutableStateFlow(FilterUiState())
    val state: StateFlow<FilterUiState> = _state.asStateFlow()

    init {
        loadSavedFilters()
    }
    private fun FilterUiState.hasAnyActive(): Boolean =
        salary.isNotEmpty() || onlyWithSalary || industryId != -1 || region.isNotEmpty()
    fun loadSavedFilters() {
        viewModelScope.launch {
            val salary = filterSettingsInteractor.getSalary()
            val onlyWithSalary = filterSettingsInteractor.getOnlyWithSalary()
            val industryName = filterSettingsInteractor.getIndustry().industryName
            val industryId = filterSettingsInteractor.getIndustry().industryId
            val countryText = filterSettingsInteractor.getRegion().countryName
            val regionText = filterSettingsInteractor.getRegion().regionName

            val regionPlaceText = regionText(countryText, regionText)

            val newState = FilterUiState(
                salary = salary,
                onlyWithSalary = onlyWithSalary,
                industry = industryName,
                industryId = industryId,
                region = regionPlaceText
            )
            _state.value = newState.copy(hasAnyFilter = newState.hasAnyActive())
        }
    }

    private fun regionText(country: String, region: String): String = buildString {
        if (country.isNotEmpty()) append(country)
        if (country.isNotEmpty() && region.isNotEmpty()) append(", ")
        if (region.isNotEmpty()) append(region)
    }

    fun updateSalary(salary: String) {
        viewModelScope.launch {
            filterSettingsInteractor.saveSalary(salary)
            _state.update {
                it.copy(
                    salary = salary,
                    hasAnyFilter = salary.isNotEmpty() || it.onlyWithSalary || it.industryId != -1
                )
            }
        }
    }

    fun updateOnlyWithSalary(checked: Boolean) {
        viewModelScope.launch {
            filterSettingsInteractor.saveOnlyWithSalary(checked)
            _state.update {
                it.copy(
                    onlyWithSalary = checked,
                    hasAnyFilter = it.salary.isNotEmpty() || checked || it.industryId != -1 || it.region.isNotEmpty()
                )
            }
        }
    }

    fun clearAllFilters() {
        viewModelScope.launch {
            filterSettingsInteractor.clearAllFilters()
            _state.update { FilterUiState(hasAnyFilter = false) }
        }
    }

    fun updateIndustry(industryName: String, industryId: Int) {
        viewModelScope.launch {
            filterSettingsInteractor.saveIndustry(industryId, industryName)
            _state.update {
                it.copy(
                    industry = industryName,
                    industryId = industryId,
                    hasAnyFilter = it.salary.isNotEmpty() || it.onlyWithSalary || industryId != -1
                )
            }
        }
    }

    private fun FilterUiState.isAnyFilterActive(): Boolean {
        return salary.isNotEmpty() ||
            onlyWithSalary ||
            industryId != -1 ||
            region.isNotEmpty()
    }

    fun updateRegion(countryId: Int, countryName: String, regionId: Int, regionName: String) {
        viewModelScope.launch {
            filterSettingsInteractor.saveRegion(countryId, countryName, regionId, regionName)
            _state.update {
                val newRegionText = regionText(countryName, regionName)
                it.copy(
                    region = newRegionText,
                    hasAnyFilter = it.copy(region = newRegionText).isAnyFilterActive()
                )
            }
        }
    }
}
