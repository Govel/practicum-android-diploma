package ru.practicum.android.diploma.favorite.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.main.domain.models.VacancyCard
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailResponse

interface FavoritesRepository {
    suspend fun addFavoritesVacancy(vacancy: VacancyDetailResponse)
    suspend fun deleteFavoritesVacancy(idVacancy: String)
    suspend fun isFavorite(idVacancy: String): Boolean
    fun getFavorites(): Flow<List<VacancyCard>>
}
