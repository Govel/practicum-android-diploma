package ru.practicum.android.diploma.filter.workplace.country.data.impl

import android.util.Log
import ru.practicum.android.diploma.filter.workplace.country.domain.api.CountryRepository
import ru.practicum.android.diploma.filter.workplace.country.domain.models.Country
import ru.practicum.android.diploma.filter.workplace.data.FilterAreasResponse
import ru.practicum.android.diploma.main.data.dto.NetworkResponse
import ru.practicum.android.diploma.main.data.network.NetworkClient
import java.io.IOException

class CountryRepositoryImpl(
    private val networkClient: NetworkClient
) : CountryRepository {

    override suspend fun getCountries(): List<Country> {
        return try {
            val response = networkClient.doRequestAreas()

            if (response.resultCode == NetworkResponse.OK_RESULT && response.data != null) {
                @Suppress("UNCHECKED_CAST")
                val areas = response.data as List<FilterAreasResponse>
                extractCountries(areas)
            } else {
                emptyList()
            }
        } catch (e: IOException) {
            Log.e("CountryRepository", "Network error: ${e.message}", e)
            emptyList()
        }
    }

    private fun extractCountries(areas: List<FilterAreasResponse>): List<Country> {
        return areas
            .filter { it.parentId == 0 || it.parentId == null }
            .map { Country(it.id, it.name) }
    }
}
