package ru.practicum.android.diploma.vacancy.domain.models

data class VacancyDetail(
    val id: String,
    val name: String,
    val description: String,
    val salary: String?,
    val address: AddressEmployer?,
    val experience: String?,
    val schedule: String?,
    val employment: String?,
    val contacts: ContactsEmployer?,
    val employer: Employer,
    val area: String,
    val skills: List<String>,
    val url: String,
    val industry: String,
    val isFavorite: Boolean? = false
)
