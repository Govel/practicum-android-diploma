package ru.practicum.android.diploma.main.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.main.data.model.VacancyFilterRequest
import ru.practicum.android.diploma.main.domain.models.VacancyCard

interface VacanciesInteractor {
    fun searchVacancies(expression: VacancyFilterRequest): Flow<Pair<List<VacancyCard>?, String?>>
}
