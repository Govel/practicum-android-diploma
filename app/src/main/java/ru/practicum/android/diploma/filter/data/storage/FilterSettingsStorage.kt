package ru.practicum.android.diploma.filter.data.storage

import android.content.SharedPreferences
import androidx.core.content.edit

class FilterSettingsStorage(private val sharedPreferences: SharedPreferences) {

    fun saveSalary(salary: String) {
        sharedPreferences.edit {
            putString(KEY_SALARY, salary)
        }
    }

    fun getSalary(): String {
        return sharedPreferences.getString(KEY_SALARY, "") ?: ""
    }

    fun saveOnlyWithSalary(checked: Boolean) {
        sharedPreferences.edit {
            putBoolean(KEY_ONLY_WITH_SALARY, checked)
        }
    }

    fun getOnlyWithSalary(): Boolean {
        return sharedPreferences.getBoolean(KEY_ONLY_WITH_SALARY, false)
    }

    fun clearAllFilters() {
        sharedPreferences.edit {
            remove(KEY_SALARY)
            remove(KEY_ONLY_WITH_SALARY)
        }
    }

    companion object {
        private const val KEY_SALARY = "filter_salary"
        private const val KEY_ONLY_WITH_SALARY = "filter_only_with_salary"

    }
}
