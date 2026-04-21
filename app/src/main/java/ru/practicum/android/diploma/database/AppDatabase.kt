package ru.practicum.android.diploma.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.database.data.dao.FavoriteVacancyDao
import ru.practicum.android.diploma.database.data.dto.FavoriteVacancyEntity

@Database(version = 1, entities = [FavoriteVacancyEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteVacancyDao(): FavoriteVacancyDao
}
