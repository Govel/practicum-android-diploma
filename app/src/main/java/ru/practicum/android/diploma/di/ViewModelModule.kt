package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.favorite.FavoritesViewModel
import ru.practicum.android.diploma.main.ui.SearchViewModel

val viewModelModule = module {
    factory { SearchViewModel(get(),androidContext()) }

    factory {
        FavoritesViewModel(get())
    }
}
