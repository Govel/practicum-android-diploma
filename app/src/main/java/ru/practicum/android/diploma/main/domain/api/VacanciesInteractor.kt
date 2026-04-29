package ru.practicum.android.diploma.main.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.main.domain.models.VacancyCard
import ru.practicum.android.diploma.main.domain.models.VacancyFilter

interface VacanciesInteractor {
    fun searchVacancies(expression: VacancyFilter): Flow<Pair<List<VacancyCard>?,  Pair<Int?, String?>?>>
}
