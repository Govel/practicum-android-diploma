package ru.practicum.android.diploma.filter.domain.api

import ru.practicum.android.diploma.filter.domain.models.Industry
import ru.practicum.android.diploma.filter.domain.models.Region

interface FilterSettingsRepository {
    fun saveSalary(salary: String)
    fun getSalary(): String
    fun saveOnlyWithSalary(checked: Boolean)
    fun getOnlyWithSalary(): Boolean
    fun clearAllFilters()
    fun hasAnyFilter(): Boolean
    fun saveIndustry(industryId: Int, industryName: String)
    fun getIndustry(): Industry
    fun getRegion(): Region
    fun saveRegion(countryId: Int, countryName: String, regionId: Int, regionName: String)
}
