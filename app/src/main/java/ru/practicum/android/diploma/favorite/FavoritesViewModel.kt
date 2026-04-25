package ru.practicum.android.diploma.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.favorite.domain.api.FavoritesInteractor
import ru.practicum.android.diploma.favorite.domain.models.VacancyCard

class FavoritesViewModel(
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private val _stateFavorite = MutableStateFlow<FavoritesState>(FavoritesState.Loading)
    val state: StateFlow<FavoritesState> = _stateFavorite.asStateFlow()

    fun loadFavorites() {
        renderState(FavoritesState.Loading)
        viewModelScope.launch {
            favoritesInteractor
                .getFavorites()
                .catch { e ->
                    e.printStackTrace()
                    _stateFavorite.value = FavoritesState.Error
                }
                .collect { value ->
                    processResult(value)
                }
        }
    }

    private fun processResult(vacancy: List<VacancyCard>) {
        if (vacancy.isNotEmpty()) {
            renderState(FavoritesState.Content(vacancy))
        } else {
            renderState(FavoritesState.Empty)
        }
    }

    private fun renderState(state: FavoritesState) {
        _stateFavorite.value = state
    }

}
