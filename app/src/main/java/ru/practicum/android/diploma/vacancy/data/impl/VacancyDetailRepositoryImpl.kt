package ru.practicum.android.diploma.vacancy.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.main.data.dto.NetworkResponse
import ru.practicum.android.diploma.main.data.mapper.VacanciesMapper
import ru.practicum.android.diploma.main.data.model.NetworkState
import ru.practicum.android.diploma.main.data.network.NetworkClient
import ru.practicum.android.diploma.main.domain.models.Resource
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailResponse
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailRepository
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetail

class VacancyDetailRepositoryImpl(
    val networkClient: NetworkClient,
    private val vm: VacanciesMapper
) : VacancyDetailRepository {
    override fun getVacancyById(vacancyId: String): Flow<Resource<VacancyDetail>?> = flow {
        val response = networkClient.doRequestVacancyById(vacancyId)
        when (response.resultCode) {
            NetworkResponse.NO_CONNECTION -> {
                emit(Resource.Error(NetworkState.NoConnection.state))
            }

            NetworkResponse.OK_RESULT -> {
                val vacanciesResponse = response.data as VacancyDetailResponse
                if (vacanciesResponse.id.isNotEmpty()) {
                    emit(Resource.Success(vm.mapVacancyDetailDtoToDomain(vacanciesResponse)))
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
