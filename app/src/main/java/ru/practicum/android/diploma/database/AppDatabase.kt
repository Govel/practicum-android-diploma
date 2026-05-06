package ru.practicum.android.diploma.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.database.data.dao.FavoriteVacancyDao
import ru.practicum.android.diploma.database.data.dto.FavoriteVacancyEntity

@Database(version = 2, entities = [FavoriteVacancyEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteVacancyDao(): FavoriteVacancyDao
    companion object {
        private const val DATABASE_NAME = "database.db"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration(true)
                .build()
        }
    }
}
