package ru.practicum.android.diploma.main.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.main.data.dto.NetworkResponse
import ru.practicum.android.diploma.main.data.dto.VacanciesResponse
import ru.practicum.android.diploma.main.data.mapper.VacanciesMapper
import ru.practicum.android.diploma.main.data.model.VacanciesSearchState
import ru.practicum.android.diploma.main.data.network.NetworkClient
import ru.practicum.android.diploma.main.domain.api.VacanciesRepository
import ru.practicum.android.diploma.main.domain.models.Resource
import ru.practicum.android.diploma.main.domain.models.VacancyCard
import ru.practicum.android.diploma.main.domain.models.VacancyFilter

class VacanciesRepositoryImpl(val networkClient: NetworkClient) : VacanciesRepository {
    override fun searchVacancies(expression: VacancyFilter): Flow<Resource<List<VacancyCard>?>> = flow {
        val response = networkClient.doRequestVacancies(expression)
        when (response.resultCode) {
            NetworkResponse.NO_CONNECTION -> {
                emit(Resource.Error(VacanciesSearchState.NoConnection.state))
            }

            NetworkResponse.OK_RESULT -> {
                val vacanciesResponse = response.data as VacanciesResponse
                if (vacanciesResponse.items.isNotEmpty()) {
                    emit(Resource.Success(VacanciesMapper.mapDtoListToDomain(vacanciesResponse.items)))
                } else {
                    emit(Resource.Error(VacanciesSearchState.Empty.state))
                }
            }

            else -> {
                emit(Resource.Error(VacanciesSearchState.Error.state))
            }
        }
    }
}
