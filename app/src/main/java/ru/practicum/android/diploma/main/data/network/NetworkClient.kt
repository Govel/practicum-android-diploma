package ru.practicum.android.diploma.main.data.network

import ru.practicum.android.diploma.main.data.dto.NetworkResponse
import ru.practicum.android.diploma.main.data.model.VacancyFilter

interface NetworkClient {
    suspend fun doRequestVacancies(filter: VacancyFilter): NetworkResponse

    suspend fun doRequestAreas(): NetworkResponse

    suspend fun doRequestIndustries(): NetworkResponse

    suspend fun doRequestVacancyById(id: Int): NetworkResponse
}
