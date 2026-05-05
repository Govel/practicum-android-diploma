package ru.practicum.android.diploma.filter.domain.api

interface FilterSettingsInteractor {
    fun saveSalary(salary: String)
    fun getSalary(): String
    fun saveOnlyWithSalary(checked: Boolean)
    fun getOnlyWithSalary(): Boolean
    fun clearAllFilters()
    fun hasAnyFilter(): Boolean
    fun saveIndustry(industryId: Int, industryName: String)
    fun getIndustryId(): Int
    fun getIndustryName(): String
}
