package ru.practicum.android.diploma.filter.industry.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.industry.domain.models.FilterIndustry

interface IndustryInteractor {
    fun getIndustries(): Flow<Pair<List<FilterIndustry>?, String?>>
}
