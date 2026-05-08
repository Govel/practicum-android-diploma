package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.database.AppDatabase
import ru.practicum.android.diploma.favorite.data.impl.FavoritesRepositoryImpl
import ru.practicum.android.diploma.favorite.domain.api.FavoritesRepository
import ru.practicum.android.diploma.filter.industry.data.impl.IndustryRepositoryImpl
import ru.practicum.android.diploma.filter.industry.domain.api.IndustryRepository
import ru.practicum.android.diploma.main.data.impl.VacanciesRepositoryImpl
import ru.practicum.android.diploma.main.domain.api.VacanciesRepository
import ru.practicum.android.diploma.vacancy.data.impl.ShareRepositoryImpl
import ru.practicum.android.diploma.vacancy.data.impl.VacancyDetailRepositoryImpl
import ru.practicum.android.diploma.vacancy.domain.api.ShareRepository
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailRepository

val repositoryModule = module {
    factory<VacanciesRepository> {
        VacanciesRepositoryImpl(get())
    }
    factory<VacancyDetailRepository> {
        VacancyDetailRepositoryImpl(get())
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

    factory<IndustryRepository> {
        IndustryRepositoryImpl(get())
    }

}
