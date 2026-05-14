package ru.practicum.android.diploma.filter.workplace.data

data class FilterAreasResponse(
    val id: Int,
    val name: String,
    val parentId: Int? = null,
    val areas: List<FilterAreasResponse> = emptyList()
)
