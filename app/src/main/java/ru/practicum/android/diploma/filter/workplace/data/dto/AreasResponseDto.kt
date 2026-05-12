package ru.practicum.android.diploma.filter.workplace.data.dto
data class AreasResponseDto(
    val id: Int,
    val name: String,
    val parentId: Int? = null,
    val areas: List<AreasResponseDto>? = emptyList()
)
