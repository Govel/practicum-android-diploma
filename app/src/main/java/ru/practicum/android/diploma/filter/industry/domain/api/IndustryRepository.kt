package ru.practicum.android.diploma.filter.industry.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.industry.domain.models.FilterIndustry
import ru.practicum.android.diploma.main.domain.models.Resource

interface IndustryRepository {
    fun getIndustries(): Flow<Resource<List<FilterIndustry>>>
}
