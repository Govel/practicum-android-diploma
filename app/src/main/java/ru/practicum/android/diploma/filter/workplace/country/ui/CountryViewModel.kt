package ru.practicum.android.diploma.filter.workplace.country.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.workplace.country.domain.api.CountryInteractor
import ru.practicum.android.diploma.filter.workplace.country.domain.models.CountriesState
import ru.practicum.android.diploma.filter.workplace.country.domain.models.Country

class CountryViewModel(
    private val interactor: CountryInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<CountriesState>(CountriesState.Loading)
    val state: StateFlow<CountriesState> = _state.asStateFlow()

    private val _selectedCountry = MutableStateFlow<Country?>(null)


    init {
        loadCountries()
    }

    fun loadCountries() {
        viewModelScope.launch {
            _state.update { CountriesState.Loading }
            val countries = interactor.getCountries()
            if (countries.isEmpty()) {
                _state.update { CountriesState.Error }
            } else {
                _state.update { CountriesState.Content(countries) }
            }
        }
    }

    fun selectCountry(country: Country) {
        _selectedCountry.update { country }
    }

}
