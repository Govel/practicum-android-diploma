package ru.practicum.android.diploma.filter.workplace.region.domain.api

import ru.practicum.android.diploma.filter.workplace.region.domain.models.FilterRegion

interface RegionFilterRepository {
    suspend fun getRegions(): List<FilterRegion>
}
