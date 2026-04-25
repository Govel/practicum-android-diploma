package ru.practicum.android.diploma.favorite.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorite.domain.api.FavoritesInteractor
import ru.practicum.android.diploma.favorite.domain.api.FavoritesRepository
import ru.practicum.android.diploma.favorite.domain.models.VacancyCard

class FavoritesInteractorImpl(
    val favoritesRepository: FavoritesRepository
) : FavoritesInteractor{
    override suspend fun addFavoritesVacancy(vacancy: VacancyCard) {
        favoritesRepository.addFavoritesVacancy(vacancy)
    }

    override suspend fun deleteFavoritesVacancy(idVacancy: String) {
        favoritesRepository.deleteFavoritesVacancy(idVacancy)
    }

    override suspend fun isFavorite(idVacancy: String): Boolean {
        return favoritesRepository.isFavorite(idVacancy)
    }

    override fun getFavorites(): Flow<List<VacancyCard>> {
        return favoritesRepository.getFavorites()
    }
}
