package ru.practicum.android.diploma.filter.domain.impl

import ru.practicum.android.diploma.filter.domain.api.FilterSettingsInteractor
import ru.practicum.android.diploma.filter.domain.api.FilterSettingsRepository

class FilterSettingsInteractorImpl(
    private val repository: FilterSettingsRepository
) : FilterSettingsInteractor {

    override fun saveSalary(salary: String) = repository.saveSalary(salary)
    override fun getSalary(): String = repository.getSalary()
    override fun saveOnlyWithSalary(checked: Boolean) = repository.saveOnlyWithSalary(checked)
    override fun getOnlyWithSalary(): Boolean = repository.getOnlyWithSalary()
    override fun clearAllFilters() = repository.clearAllFilters()
    override fun hasAnyFilter(): Boolean = repository.hasAnyFilter()
}
