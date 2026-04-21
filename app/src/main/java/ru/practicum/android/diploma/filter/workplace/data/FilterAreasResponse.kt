package ru.practicum.android.diploma.filter.workplace.data

import ru.practicum.android.diploma.main.data.dto.NetworkResponse

data class FilterAreasResponse(
    val id: Int,
    val name: String,
    val parentId: Int,
    val areas: List<FilterAreasResponse>?
) : NetworkResponse()
