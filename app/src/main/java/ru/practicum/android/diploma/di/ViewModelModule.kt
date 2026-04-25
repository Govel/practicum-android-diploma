package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorite.FavoritesViewModel
import ru.practicum.android.diploma.main.ui.SearchViewModel

val viewModelModule = module {
    factory {
        FavoritesViewModel(get())
    }
    factory { SearchViewModel(get()) }

}
