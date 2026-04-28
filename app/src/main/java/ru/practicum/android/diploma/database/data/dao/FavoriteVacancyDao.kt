package ru.practicum.android.diploma.database.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.database.data.dto.FavoriteVacancyEntity

@Dao
interface FavoriteVacancyDao {
    @Query("SELECT * FROM favorite_vacancy_table")
    suspend fun getFavoriteVacancy(): List<FavoriteVacancyEntity>

    @Insert(entity = FavoriteVacancyEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteVacancy(favoriteVacancy: FavoriteVacancyEntity)

    @Query("DELETE FROM favorite_vacancy_table WHERE id = :id")
    suspend fun deleteFavoriteVacancyById(id: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_vacancy_table WHERE id = :id)")
    suspend fun isFavorites(id: String): Boolean
}
