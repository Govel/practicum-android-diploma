package ru.practicum.android.diploma.filter.industry.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.filter.industry.domain.api.IndustryInteractor
import ru.practicum.android.diploma.filter.industry.domain.api.IndustryRepository
import ru.practicum.android.diploma.main.domain.models.Resource

class IndustryInteractorImpl(private val industryRepository: IndustryRepository) : IndustryInteractor {
    override fun getIndustries(): Flow<Pair<List<String>?, String?>> {
        return industryRepository.getIndustries().map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}
