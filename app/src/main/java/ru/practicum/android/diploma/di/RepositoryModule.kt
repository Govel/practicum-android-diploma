package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.database.AppDatabase
import ru.practicum.android.diploma.favorite.data.FavoritesDbConverter
import ru.practicum.android.diploma.favorite.data.impl.FavoritesRepositoryImpl
import ru.practicum.android.diploma.favorite.domain.api.FavoritesRepository

val repositoryModule = module {

    factory { FavoritesDbConverter() }

    single {
        get<AppDatabase>().favoriteVacancyDao()
    }
    factory<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }
}
