package ru.practicum.android.diploma.filter.workplace.region.domain.models

sealed interface RegionsState {
    data class Content(val regions: List<FilterRegion>) : RegionsState
    object Error : RegionsState
    object Empty : RegionsState
    object Loading : RegionsState
}
