package ru.practicum.android.diploma.filter.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.FilterSettingsInteractor

data class FilterUiState(
    val salary: String = "",
    val onlyWithSalary: Boolean = false,
    val hasAnyFilter: Boolean = false,
    val industry: String = "",
    val industryId: Int = -1,
)

class FilterViewModel(
    private val filterSettingsInteractor: FilterSettingsInteractor
) : ViewModel() {

    private val _state = MutableStateFlow(FilterUiState())
    val state: StateFlow<FilterUiState> = _state.asStateFlow()

    init {
        loadSavedFilters()
    }

    private fun loadSavedFilters() {
        viewModelScope.launch {
            val salary = filterSettingsInteractor.getSalary()
            val onlyWithSalary = filterSettingsInteractor.getOnlyWithSalary()
            val industryName = filterSettingsInteractor.getIndustryName()
            val industryId = filterSettingsInteractor.getIndustryId()

            _state.update {
                it.copy(
                    salary = salary,
                    onlyWithSalary = onlyWithSalary,
                    industry = industryName,
                    industryId = industryId,
                    hasAnyFilter = salary.isNotEmpty() || onlyWithSalary || industryId != -1
                )
            }
        }
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
                    hasAnyFilter = it.salary.isNotEmpty() || checked || it.industryId != -1
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
}
