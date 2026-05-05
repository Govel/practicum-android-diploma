package ru.practicum.android.diploma.filter.industry.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.filter.industry.data.dto.FilterIndustryResponse
import ru.practicum.android.diploma.filter.industry.data.mapper.FilterMapper
import ru.practicum.android.diploma.filter.industry.domain.api.IndustryRepository
import ru.practicum.android.diploma.filter.industry.domain.models.FilterIndustry
import ru.practicum.android.diploma.main.data.dto.NetworkResponse
import ru.practicum.android.diploma.main.data.model.NetworkState
import ru.practicum.android.diploma.main.data.network.NetworkClient
import ru.practicum.android.diploma.main.domain.models.Resource

class IndustryRepositoryImpl(val networkClient: NetworkClient) : IndustryRepository {
    override fun getIndustries(): Flow<Resource<List<FilterIndustry>>> = flow {
        val response = networkClient.doRequestIndustries()
        when (response.resultCode) {
            NetworkResponse.NO_CONNECTION -> {
                emit(Resource.Error(NetworkState.NoConnection.state))
            }

            NetworkResponse.OK_RESULT -> {
                val industryResponse = response.data as List<FilterIndustryResponse>
                if (industryResponse.isNotEmpty()) {
                    emit(Resource.Success(FilterMapper.mapListFilterIndustryToDomain(industryResponse)))
                } else {
                    emit(Resource.Error(NetworkState.Empty.state))
                }
            }

            else -> {
                emit(Resource.Error(NetworkState.Error.state))
            }
        }
    }
}
