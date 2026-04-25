package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorite.domain.api.FavoritesInteractor
import ru.practicum.android.diploma.favorite.domain.impl.FavoritesInteractorImpl

val interactorModule = module {
    factory<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }
}
