package ru.practicum.android.diploma.filter.data.impl

import android.content.Context
import androidx.core.content.edit
import ru.practicum.android.diploma.filter.domain.api.FilterSettingsRepository
import ru.practicum.android.diploma.filter.domain.models.Industry
import ru.practicum.android.diploma.filter.domain.models.Region

class FilterSettingsRepositoryImpl(
    context: Context
) : FilterSettingsRepository {

    private val prefs = context.getSharedPreferences("filter_settings", Context.MODE_PRIVATE)

    override fun saveSalary(salary: String) {
        prefs.edit { putString("salary", salary) }
    }

    override fun getSalary(): String {
        return prefs.getString("salary", "") ?: ""
    }

    override fun saveOnlyWithSalary(checked: Boolean) {
        prefs.edit { putBoolean("only_with_salary", checked) }
    }

    override fun getOnlyWithSalary(): Boolean {
        return prefs.getBoolean("only_with_salary", false)
    }

    override fun clearAllFilters() {
        prefs.edit { clear() }
    }

    override fun saveIndustry(industryId: Int, industryName: String) {
        prefs.edit {
            putInt("industry_id", industryId)
                .putString("industry_name", industryName)
        }
    }

    override fun getIndustry(): Industry {
        return Industry(
            industryId = prefs.getInt("industry_id", -1),
            industryName = prefs.getString("industry_name", "") ?: ""
        )
    }

    override fun getRegion(): Region {
        return Region(
            countryId = prefs.getInt("country_id", -1),
            countryName = prefs.getString("country_name", "") ?: "",
            regionId = prefs.getInt("region_id", -1),
            regionName = prefs.getString("region_name", "") ?: ""
        )
    }

    override fun saveRegion(countryId: Int, countryName: String, regionId: Int, regionName: String) {
        prefs.edit {
            putInt("country_id", countryId)
                .putString("country_name", countryName)
                .putInt("region_id", regionId)
                .putString("region_name", regionName)
        }
    }

    override fun hasAnyFilter(): Boolean {
        return getSalary().isNotEmpty() ||
            getOnlyWithSalary() || getIndustry().industryId != -1 || getRegion().regionId != -1
    }
}
