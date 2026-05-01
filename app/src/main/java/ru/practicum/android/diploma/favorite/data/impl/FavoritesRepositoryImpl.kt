package ru.practicum.android.diploma.favorite.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.database.data.dao.FavoriteVacancyDao
import ru.practicum.android.diploma.favorite.domain.api.FavoritesRepository
import ru.practicum.android.diploma.main.data.mapper.VacanciesMapper
import ru.practicum.android.diploma.main.domain.models.VacancyCard
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetail

class FavoritesRepositoryImpl(
    private val favoriteVacancyDao: FavoriteVacancyDao
) : FavoritesRepository {
    override suspend fun addFavoritesVacancy(vacancy: VacancyDetail) {
        favoriteVacancyDao.insertFavoriteVacancy(VacanciesMapper.mapDetailToEntity(vacancy))
    }

    override suspend fun deleteFavoritesVacancy(idVacancy: String) {
        favoriteVacancyDao.deleteFavoriteVacancyById(idVacancy)
    }

    override suspend fun isFavorite(idVacancy: String): Boolean {
        return favoriteVacancyDao.isFavorites(idVacancy)
    }

    override fun getFavorites(): Flow<List<VacancyCard>> = flow {
        val favorites = favoriteVacancyDao.getFavoriteVacancy()
        emit(VacanciesMapper.mapEntityListToDomain(favorites))
    }

    override suspend fun getFavoriteById(id: String): VacancyDetail? {
        val entity = favoriteVacancyDao.getFavoriteById(id)
        return entity?.let { VacanciesMapper.mapEntityToDetail(it) }
    }
}
