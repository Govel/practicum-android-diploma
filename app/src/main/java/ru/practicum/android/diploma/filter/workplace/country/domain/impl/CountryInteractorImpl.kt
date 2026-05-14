package ru.practicum.android.diploma.filter.workplace.country.domain.impl

import ru.practicum.android.diploma.filter.workplace.country.domain.api.CountryInteractor
import ru.practicum.android.diploma.filter.workplace.country.domain.api.CountryRepository
import ru.practicum.android.diploma.filter.workplace.country.domain.models.Country

class CountryInteractorImpl(
    private val repository: CountryRepository
) : CountryInteractor {
    override suspend fun getCountries(): List<Country> {
        return repository.getCountries()
    }
}
