package ru.practicum.android.diploma.favorite.domain.models

import ru.practicum.android.diploma.main.data.dto.VacancyCardSalary

data class VacancyCard(
    val id: String,
    val name: String,
    val company: String?,
    val city: String,
    val salary: VacancyCardSalary?,
    val logo: String?
)

