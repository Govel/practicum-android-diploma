package ru.practicum.android.diploma.filter.workplace.region.domain.api

import ru.practicum.android.diploma.filter.workplace.region.domain.models.FilterRegion

interface RegionFilterInteractor {
    suspend fun getRegions(): List<FilterRegion>
}
