package ru.practicum.android.diploma.main.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.main.data.model.VacancyFilterRequest
import ru.practicum.android.diploma.main.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.main.domain.api.VacanciesRepository
import ru.practicum.android.diploma.main.domain.models.Resource
import ru.practicum.android.diploma.main.domain.models.VacancyCard

class VacanciesInteractorImpl(private val vacanciesRepository: VacanciesRepository) : VacanciesInteractor {
    override fun searchVacancies(expression: VacancyFilterRequest): Flow<Pair<List<VacancyCard>?, String?>> {
        return vacanciesRepository.searchVacancies(expression).map { result ->
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
