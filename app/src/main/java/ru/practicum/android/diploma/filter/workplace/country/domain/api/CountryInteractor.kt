package ru.practicum.android.diploma.filter.workplace.country.domain.api

import ru.practicum.android.diploma.filter.workplace.country.domain.models.Country

interface CountryInteractor {
    suspend fun getCountries(): List<Country>
}
