package ru.practicum.android.diploma.filter.industry.domain.api

import kotlinx.coroutines.flow.Flow

interface IndustryInteractor {
    fun getIndustries(): Flow<Pair<List<String>?, String?>>
}
