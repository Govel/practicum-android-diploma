package ru.practicum.android.diploma.filter.data.impl

import ru.practicum.android.diploma.filter.data.storage.FilterSettingsStorage
import ru.practicum.android.diploma.filter.domain.api.FilterSettingsRepository

class FilterSettingsRepositoryImpl(
    private val storage: FilterSettingsStorage
) : FilterSettingsRepository {

    override fun saveSalary(salary: String) = storage.saveSalary(salary)
    override fun getSalary(): String = storage.getSalary()
    override fun saveOnlyWithSalary(checked: Boolean) = storage.saveOnlyWithSalary(checked)
    override fun getOnlyWithSalary(): Boolean = storage.getOnlyWithSalary()

    override fun clearAllFilters() = storage.clearAllFilters()

    override fun saveIndustry(industryId: Int, industryName: String) = storage.saveIndustry(industryId, industryName)
    override fun getIndustryId(): Int = storage.getIndustryId()
    override fun getIndustryName(): String = storage.getIndustryName()

    override fun hasAnyFilter(): Boolean {
        return getSalary().isNotEmpty() ||
            getOnlyWithSalary() || getIndustryId() != -1
    }
}
