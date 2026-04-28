package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.database.AppDatabase
import ru.practicum.android.diploma.favorite.data.impl.FavoritesRepositoryImpl
import ru.practicum.android.diploma.favorite.domain.api.FavoritesRepository
import ru.practicum.android.diploma.main.data.impl.VacancyRepositoryImpl
import ru.practicum.android.diploma.main.domain.api.VacanciesRepository
import ru.practicum.android.diploma.vacancy.data.impl.ShareRepositoryImpl
import ru.practicum.android.diploma.vacancy.domain.api.ShareRepository

val repositoryModule = module {
    factory<VacanciesRepository> {
        VacancyRepositoryImpl(get())
    }

    single {
        get<AppDatabase>().favoriteVacancyDao()
    }

    factory<FavoritesRepository> {
        FavoritesRepositoryImpl(get())
    }

    factory<ShareRepository> {
        ShareRepositoryImpl(androidContext())
    }
}
