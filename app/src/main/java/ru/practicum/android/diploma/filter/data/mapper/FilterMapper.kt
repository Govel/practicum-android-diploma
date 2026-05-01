package ru.practicum.android.diploma.filter.data.mapper

import ru.practicum.android.diploma.filter.industry.data.dto.FilterIndustryResponse

object FilterMapper {
    fun mapListFilterIndustryToDomain(listFilterIndustryDto: List<FilterIndustryResponse>): List<String> {
        return listFilterIndustryDto.map { industryDto -> industryDto.name }
    }
}
