package ru.practicum.android.diploma.filter.industry.domain.models

sealed interface IndustriesState {
    data class Content(val industries: List<FilterIndustry>) : IndustriesState
    object Error : IndustriesState
    object Empty : IndustriesState
    object Loading : IndustriesState
}
