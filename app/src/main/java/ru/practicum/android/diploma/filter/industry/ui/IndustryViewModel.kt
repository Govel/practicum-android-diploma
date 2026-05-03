package ru.practicum.android.diploma.filter.industry.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.industry.domain.api.IndustryInteractor
import ru.practicum.android.diploma.filter.industry.domain.models.FilterIndustry
import ru.practicum.android.diploma.filter.industry.domain.models.IndustriesState
import ru.practicum.android.diploma.main.data.model.NetworkState

class IndustryViewModel(
    private val interactor: IndustryInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<IndustriesState>(IndustriesState.Loading)
    val state: StateFlow<IndustriesState> = _state.asStateFlow()

    var currentIndustries: List<FilterIndustry>? = emptyList()

    fun loadIndustries() {
        viewModelScope.launch {
            interactor.getIndustries()
                .catch {
                    _state.update {
                        IndustriesState.Error
                    }
                }
                .collect { result ->
                    currentIndustries = result.first
                    handleIndustriesResult(result)
                }
        }
    }

    private fun handleIndustriesResult(result: Pair<List<FilterIndustry>?, String?>) {
        val (industries, error) = result
        if (error != null && error != NetworkState.Empty.state) {
            _state.update { IndustriesState.Error }
            return
        }
        _state.update { IndustriesState.Content(industries) }
    }
}
