package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorite.domain.api.FavoritesInteractor
import ru.practicum.android.diploma.favorite.domain.impl.FavoritesInteractorImpl
import ru.practicum.android.diploma.main.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.main.domain.impl.VacanciesInteractorImpl
import ru.practicum.android.diploma.vacancy.domain.api.ShareInteractor
import ru.practicum.android.diploma.vacancy.domain.impl.ShareInteractorImpl

val interactorModule = module {
    factory<VacanciesInteractor> {
        VacanciesInteractorImpl(get())
    }

    factory<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }

    factory<ShareInteractor> {
        ShareInteractorImpl(get())
    }
}
