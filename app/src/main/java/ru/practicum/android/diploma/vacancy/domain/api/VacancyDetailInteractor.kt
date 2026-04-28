package ru.practicum.android.diploma.vacancy.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetail

interface VacancyDetailInteractor {
    fun getVacancyById(vacancyId: String): Flow<Pair<VacancyDetail?, String?>>
}
