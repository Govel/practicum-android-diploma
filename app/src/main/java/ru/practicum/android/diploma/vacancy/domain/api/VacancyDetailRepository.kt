package ru.practicum.android.diploma.vacancy.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.main.domain.models.Resource
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetail

interface VacancyDetailRepository {
    fun getVacancyById(vacancyId: String): Flow<Resource<VacancyDetail>?>
}
