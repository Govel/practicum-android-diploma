package ru.practicum.android.diploma.filter.workplace.region.data.impl

import android.util.Log
import ru.practicum.android.diploma.filter.domain.api.FilterSettingsRepository
import ru.practicum.android.diploma.filter.workplace.data.FilterAreasResponse
import ru.practicum.android.diploma.filter.workplace.region.domain.api.RegionFilterRepository
import ru.practicum.android.diploma.filter.workplace.region.domain.models.FilterRegion
import ru.practicum.android.diploma.main.data.dto.NetworkResponse
import ru.practicum.android.diploma.main.data.network.NetworkClient
import java.io.IOException

class RegionFilterRepositoryImpl(
    private val networkClient: NetworkClient,
    private val filterRepository: FilterSettingsRepository
) : RegionFilterRepository {
    override suspend fun getRegions(): List<FilterRegion> {
        return try {
            val response = networkClient.doRequestAreas()

            if (response.resultCode == NetworkResponse.OK_RESULT && response.data != null) {
                @Suppress("UNCHECKED_CAST")
                val regions = response.data as List<FilterAreasResponse>
                extractRegion(regions)
            } else {
                emptyList()
            }
        } catch (e: IOException) {
            Log.e("CountryRepository", "Network error: ${e.message}", e)
            emptyList()
        }
    }

    private fun extractCountries(areas: List<FilterAreasResponse>): List<FilterAreasResponse> {
        val parentId = filterRepository.getRegion().countryId
        return areas.find { it.id == parentId }?.areas ?: emptyList()
    }

    private fun extractRegion(areas: List<FilterAreasResponse>): List<FilterRegion> {
        val regions = extractCountries(areas)
        return regions.map { region ->
            FilterRegion(region.id, region.name)
        }
    }

}
