package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorite.data.impl.FavoritesRepositoryImpl
import ru.practicum.android.diploma.favorite.domain.api.FavoritesRepository
import ru.practicum.android.diploma.main.data.impl.VacancyRepositoryImpl
import ru.practicum.android.diploma.main.domain.api.VacanciesRepository

val repositoryModule = module {
    factory <FavoritesRepository>{
        FavoritesRepositoryImpl(get())
    }
    factory<VacanciesRepository> {
        VacancyRepositoryImpl(get())
    }
}
