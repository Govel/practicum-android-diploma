package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorite.FavoritesViewModel

val viewModelModule = module {
    factory {
        FavoritesViewModel(get())
    }
}
