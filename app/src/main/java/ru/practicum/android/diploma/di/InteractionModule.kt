package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorite.domain.api.FavoritesInteractor
import ru.practicum.android.diploma.favorite.domain.impl.FavoritesInteractorImpl
import ru.practicum.android.diploma.main.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.main.domain.impl.VacanciesInteractorImpl
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailInteractor
import ru.practicum.android.diploma.vacancy.domain.impl.VacancyDetailInteractorImpl

val interactorModule = module {
    factory<VacanciesInteractor> {
        VacanciesInteractorImpl(get())
    }
    factory<VacancyDetailInteractor> {
        VacancyDetailInteractorImpl(get())

    factory<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }
}
