package ru.practicum.android.diploma.main.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.main.domain.models.Resource
import ru.practicum.android.diploma.main.domain.models.VacancyCard
import ru.practicum.android.diploma.main.domain.models.VacancyFilter

interface VacanciesRepository {
    fun searchVacancies(expression: VacancyFilter): Flow<Resource<Pair<List<VacancyCard>?, Int>?>>
}
