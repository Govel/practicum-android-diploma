package ru.practicum.android.diploma.vacancy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.favorite.domain.api.FavoritesInteractor
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetail

class VacancyViewModel(
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private val _stateFavorite = MutableStateFlow(false)
    val stateFavorite: StateFlow<Boolean> = _stateFavorite.asStateFlow()

    fun onFavoriteClicked(vacancy: VacancyDetail) {
        viewModelScope.launch {
            val thisFavorites = favoritesInteractor.isFavorite(vacancy.id)
            if (thisFavorites) {
                favoritesInteractor.deleteFavoritesVacancy(vacancy.id)
            } else {
                favoritesInteractor.addFavoritesVacancy(vacancy)
            }
            _stateFavorite.value = !thisFavorites
        }
    }
}
