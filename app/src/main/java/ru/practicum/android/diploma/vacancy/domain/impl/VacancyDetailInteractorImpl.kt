package ru.practicum.android.diploma.vacancy.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.main.domain.models.Resource
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailInteractor
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailRepository
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetail

class VacancyDetailInteractorImpl(private val vacancyRepository: VacancyDetailRepository) : VacancyDetailInteractor {
    override fun getVacancyById(vacancyId: String): Flow<Pair<VacancyDetail?, String?>> {
        return vacancyRepository.getVacancyById(vacancyId).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }

                else -> Pair(null, result?.message)
            }
        }
    }
}
