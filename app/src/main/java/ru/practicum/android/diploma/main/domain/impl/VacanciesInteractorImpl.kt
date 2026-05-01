package ru.practicum.android.diploma.main.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.main.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.main.domain.api.VacanciesRepository
import ru.practicum.android.diploma.main.domain.models.Resource
import ru.practicum.android.diploma.main.domain.models.VacancyCard
import ru.practicum.android.diploma.main.domain.models.VacancyFilter

class VacanciesInteractorImpl(private val vacanciesRepository: VacanciesRepository) : VacanciesInteractor {
    override fun searchVacancies(expression: VacancyFilter): Flow<Pair<List<VacancyCard>?, Pair<Int?, String?>?>> {
        return vacanciesRepository.searchVacancies(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    val (vacancies, totalFound) = result.data ?: Pair(null, null)
                    Pair(vacancies, Pair(totalFound, null))
                }

                is Resource.Error -> {
                    Pair(null, Pair(null, result.message))
                }
            }
        }
    }
}
