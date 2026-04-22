package ru.practicum.android.diploma.vacancy.data.dto

data class Contacts(
    val id: String,
    val name: String,
    val email: String,
    val phones: List<Phone>
)
