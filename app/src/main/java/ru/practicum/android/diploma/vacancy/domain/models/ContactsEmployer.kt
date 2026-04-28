package ru.practicum.android.diploma.vacancy.domain.models

data class ContactsEmployer(
    val name: String,
    val email: String,
    val phones: List<Phone>?
)
