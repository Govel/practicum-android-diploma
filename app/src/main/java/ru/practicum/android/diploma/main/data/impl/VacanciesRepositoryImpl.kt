package ru.practicum.android.diploma.main.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.main.data.dto.NetworkResponse
import ru.practicum.android.diploma.main.data.dto.VacanciesResponse
import ru.practicum.android.diploma.main.data.mapper.VacanciesMapper
import ru.practicum.android.diploma.main.data.model.NetworkState
import ru.practicum.android.diploma.main.data.network.NetworkClient
import ru.practicum.android.diploma.main.domain.api.VacanciesRepository
import ru.practicum.android.diploma.main.domain.models.Resource
import ru.practicum.android.diploma.main.domain.models.VacancyCard
import ru.practicum.android.diploma.main.domain.models.VacancyFilter

class VacanciesRepositoryImpl(val networkClient: NetworkClient) : VacanciesRepository {
    override fun searchVacancies(expression: VacancyFilter): Flow<Resource<Pair<List<VacancyCard>?, Int>?>> = flow {
        val response = networkClient.doRequestVacancies(expression)
        when (response.resultCode) {
            NetworkResponse.NO_CONNECTION -> {
                emit(Resource.Error(NetworkState.NoConnection.state))
            }

            NetworkResponse.OK_RESULT -> {
                val vacanciesResponse = response.data as VacanciesResponse
                if (vacanciesResponse.items.isNotEmpty()) {
                    val vacancies = VacanciesMapper.mapDtoListToDomain(vacanciesResponse.items)
                    emit(Resource.Success(Pair(vacancies, vacanciesResponse.found)))
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
