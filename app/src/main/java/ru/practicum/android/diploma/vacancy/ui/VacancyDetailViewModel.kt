package ru.practicum.android.diploma.vacancy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.favorite.domain.api.FavoritesInteractor
import ru.practicum.android.diploma.main.data.model.VacanciesSearchState
import ru.practicum.android.diploma.vacancy.domain.api.ShareInteractor
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailInteractor
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetail

class VacancyDetailViewModel(
    private val interactor: VacancyDetailInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val shareInteractor: ShareInteractor
) : ViewModel() {
    private val _state = MutableStateFlow<VacancyState>(VacancyState.Loading)
    val state: StateFlow<VacancyState> = _state.asStateFlow()

    private val _isFavorite = MutableStateFlow<Boolean>(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()
    var currentVacancy: VacancyDetail? = null

    var currentVacancyId: String? = null

    fun loadVacancyDetail() {
        viewModelScope.launch {
            interactor.getVacancyById(currentVacancyId ?: "")
                .catch {
                    _state.update {
                        VacancyState.Error
                    }
                }
                .collect { result ->
                    currentVacancy = result.first
                    handleVacancyDetailResult(result)

                    currentVacancy?.let { vacancy ->
                        val isFav = favoritesInteractor.isFavorite(vacancy.id)
                        _isFavorite.value = isFav
                    }
                }
        }
    }

    private fun handleVacancyDetailResult(result: Pair<VacancyDetail?, String?>) {
        val (vacancy, error) = result
        if (error != null && error != VacanciesSearchState.Empty.state) {
            _state.update { VacancyState.Error }
            return
        }
        _state.update { VacancyState.Content(vacancy) }
    }

    fun shareLink(url: String) {
        if (url.isNotEmpty()) shareInteractor.shareLink(url)
    }

    fun onFavoriteClicked(vacancy: VacancyDetail) {
        viewModelScope.launch {
            val isFav = favoritesInteractor.isFavorite(vacancy.id)
            if (isFav) {
                favoritesInteractor.deleteFavoritesVacancy(vacancy.id)
            } else {
                favoritesInteractor.addFavoritesVacancy(vacancy)
            }
            _isFavorite.value = !isFav
        }
    }
}
