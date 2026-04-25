package ru.practicum.android.diploma.favorite.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.database.data.dao.FavoriteVacancyDao
import ru.practicum.android.diploma.database.data.dto.FavoriteVacancyEntity
import ru.practicum.android.diploma.favorite.data.FavoritesDbConverter
import ru.practicum.android.diploma.favorite.domain.api.FavoritesRepository
import ru.practicum.android.diploma.favorite.domain.models.VacancyCard
import kotlin.collections.map

class FavoritesRepositoryImpl(
    private val favoriteVacancyDao: FavoriteVacancyDao,
    private val dbConverter: FavoritesDbConverter
) : FavoritesRepository {
    override suspend fun addFavoritesVacancy(vacancy: VacancyCard) {
        favoriteVacancyDao.insertFavoriteVacancy(dbConverter.map(vacancy))
    }

    override suspend fun deleteFavoritesVacancy(idVacancy: String) {
        favoriteVacancyDao.deleteFavoriteVacancyById(idVacancy)
    }

    override suspend fun isFavorite(idVacancy: String): Boolean {
        return favoriteVacancyDao.isFavorites(idVacancy)
    }

    override fun getFavorites(): Flow<List<VacancyCard>> = flow {
       val favoritesList = favoriteVacancyDao.getFavoriteVacancy()
       emit(converterFromVacancy(favoritesList))
    }

    private fun converterFromVacancy(favorites: List<FavoriteVacancyEntity>):List<VacancyCard>{
        return favorites.map { favorite -> dbConverter.map(favorite)}
    }
}
