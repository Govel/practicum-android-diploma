package ru.practicum.android.diploma.filter.workplace.country.domain.models

sealed interface CountriesState {
    data class Content(val countries: List<Country>) : CountriesState
    object Error : CountriesState
    object Loading : CountriesState
}
