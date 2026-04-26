package ru.practicum.android.diploma.favorite.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.main.domain.models.VacancyCard

interface FavoritesInteractor {
    suspend fun addFavoritesVacancy(vacancy: VacancyCard)
    suspend fun deleteFavoritesVacancy(idVacancy: String)
    suspend fun isFavorite(idVacancy: String): Boolean
    fun getFavorites(): Flow<List<VacancyCard>>
}
