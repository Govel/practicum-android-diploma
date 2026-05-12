package ru.practicum.android.diploma.filter.domain.impl

import ru.practicum.android.diploma.filter.domain.api.FilterSettingsInteractor
import ru.practicum.android.diploma.filter.domain.api.FilterSettingsRepository
import ru.practicum.android.diploma.filter.domain.models.Industry
import ru.practicum.android.diploma.filter.domain.models.Region

class FilterSettingsInteractorImpl(
    private val repository: FilterSettingsRepository
) : FilterSettingsInteractor {
    override fun saveSalary(salary: String) = repository.saveSalary(salary)

    override fun getSalary(): String = repository.getSalary()

    override fun saveOnlyWithSalary(checked: Boolean) = repository.saveOnlyWithSalary(checked)

    override fun getOnlyWithSalary(): Boolean = repository.getOnlyWithSalary()

    override fun clearAllFilters() = repository.clearAllFilters()

    override fun hasAnyFilter(): Boolean = repository.hasAnyFilter()

    override fun saveIndustry(industryId: Int, industryName: String) = repository.saveIndustry(industryId, industryName)

    override fun getIndustry(): Industry {
        return Industry(
            industryId = repository.getIndustry().industryId,
            industryName = repository.getIndustry().industryName
        )
    }

    override fun getRegion(): Region {
        return Region(
            countryId = repository.getRegion().countryId,
            countryName = repository.getRegion().countryName,
            regionId = repository.getRegion().regionId,
            regionName = repository.getRegion().regionName
        )
    }

    override fun saveRegion(
        countryId: Int,
        countryName: String,
        regionId: Int,
        regionName: String
    ) {
        return repository.saveRegion(countryId, countryName, regionId, regionName)
    }

}
