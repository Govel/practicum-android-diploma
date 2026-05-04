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
    val hasAnyFilter: Boolean = false
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

            _state.update {
                it.copy(
                    salary = salary,
                    onlyWithSalary = onlyWithSalary,
                    hasAnyFilter = salary.isNotEmpty() || onlyWithSalary
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
                    hasAnyFilter = salary.isNotEmpty() || it.onlyWithSalary
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
                    hasAnyFilter = it.salary.isNotEmpty() || checked
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
}
