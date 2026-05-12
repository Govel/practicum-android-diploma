package ru.practicum.android.diploma.filter.workplace.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.practicum.android.diploma.filter.workplace.country.domain.models.Country

class WorkPlaceViewModel : ViewModel() {

    private val _selectedCountry = MutableStateFlow<Country?>(null)
    val selectedCountry: StateFlow<Country?> = _selectedCountry.asStateFlow()

    fun selectCountry(country: Country?) {
        _selectedCountry.update { country }
    }
}
