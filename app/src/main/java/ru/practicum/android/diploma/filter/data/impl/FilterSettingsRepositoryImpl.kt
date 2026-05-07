package ru.practicum.android.diploma.filter.data.impl

import android.content.Context
import androidx.core.content.edit
import ru.practicum.android.diploma.filter.domain.api.FilterSettingsRepository

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

    override fun getIndustryId(): Int {
        return prefs.getInt("industry_id", -1)
    }


    override fun getIndustryName(): String {
        return prefs.getString("industry_name", "") ?: ""
    }

    override fun hasAnyFilter(): Boolean {
        return getSalary().isNotEmpty() ||
            getOnlyWithSalary() || getIndustryId() != -1
    }
}
