package ru.practicum.android.diploma.filter.industry.data.dto

import ru.practicum.android.diploma.main.data.dto.NetworkResponse

data class FilterIndustryResponse(
    val id: Int,
    val name: String
) : NetworkResponse()
