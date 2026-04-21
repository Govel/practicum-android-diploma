package ru.practicum.android.diploma.vacancy.data.dto

data class FilterArea(
    val id: Int,
    val name: String,
    val parentId: Int,
    val areas: List<FilterArea>
)
