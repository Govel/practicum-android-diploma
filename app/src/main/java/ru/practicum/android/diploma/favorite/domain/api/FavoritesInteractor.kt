package ru.practicum.android.diploma.favorite.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.main.domain.models.VacancyCard
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetail

interface FavoritesInteractor {
    suspend fun addFavoritesVacancy(vacancy: VacancyDetail)
    suspend fun deleteFavoritesVacancy(idVacancy: String)
    suspend fun isFavorite(idVacancy: String): Boolean
    fun getFavorites(): Flow<List<VacancyCard>>
    suspend fun getFavoriteById(id: String): VacancyDetail?
}
