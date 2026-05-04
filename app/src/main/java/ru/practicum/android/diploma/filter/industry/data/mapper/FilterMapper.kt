package ru.practicum.android.diploma.filter.data.mapper

import ru.practicum.android.diploma.filter.industry.data.dto.FilterIndustryResponse
import ru.practicum.android.diploma.filter.industry.domain.models.FilterIndustry

object FilterMapper {
    fun mapListFilterIndustryToDomain(listFilterIndustryDto: List<FilterIndustryResponse>): List<FilterIndustry> {
        return listFilterIndustryDto.map { industryDto ->
            mapFilterIndustryDtoToDomain(industryDto)
        }
    }

    fun mapFilterIndustryDtoToDomain(filterIndustryDto: FilterIndustryResponse): FilterIndustry {
        return FilterIndustry(
            id = filterIndustryDto.id,
            name = filterIndustryDto.name
        )
    }
}
