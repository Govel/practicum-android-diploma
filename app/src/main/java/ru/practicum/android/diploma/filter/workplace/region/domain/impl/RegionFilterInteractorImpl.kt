package ru.practicum.android.diploma.filter.workplace.region.domain.impl

import ru.practicum.android.diploma.filter.workplace.region.domain.api.RegionFilterInteractor
import ru.practicum.android.diploma.filter.workplace.region.domain.api.RegionFilterRepository
import ru.practicum.android.diploma.filter.workplace.region.domain.models.FilterRegion

class RegionFilterInteractorImpl(
    private val repository: RegionFilterRepository
) : RegionFilterInteractor {
    override suspend fun getRegions(): List<FilterRegion> {
        return repository.getRegions()
    }
}
