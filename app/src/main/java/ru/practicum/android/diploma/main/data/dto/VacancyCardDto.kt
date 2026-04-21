package ru.practicum.android.diploma.main.data.dto

data class VacancyCardDto(
    val id: String,
    val name: String,
    val company: String?,
    val city: String?,
    val salary: VacancyCardSalary?,
    val logo: String?,
)
