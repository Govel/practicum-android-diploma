package ru.practicum.android.diploma.main.data.dto

data class VacanciesResponse(
    val found: Int,
    val pages: Int,
    val page: Int,
    val items: List<VacancyCardDto>
) : NetworkResponse()
