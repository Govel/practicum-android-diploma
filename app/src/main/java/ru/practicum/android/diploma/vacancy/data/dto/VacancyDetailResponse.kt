package ru.practicum.android.diploma.vacancy.data.dto

import ru.practicum.android.diploma.main.data.dto.NetworkResponse

data class VacancyDetailResponse(
    val id: String,
    val name: String,
    val description: String,
    val salary: Salary?,
    val address: Address?,
    val experience: Experience?,
    val schedule: Schedule?,
    val employment: Employment?,
    val contacts: Contacts?,
    val employer: Employer,
    val area: FilterArea,
    val skills: List<String>,
    val url: String,
    val industry: FilterIndustry,
) : NetworkResponse()
