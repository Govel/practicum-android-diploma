package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.favorite.FavoritesViewModel
import ru.practicum.android.diploma.filter.industry.ui.IndustryViewModel
import ru.practicum.android.diploma.filter.ui.FilterViewModel
import ru.practicum.android.diploma.filter.workplace.region.RegionViewModel
import ru.practicum.android.diploma.main.ui.SearchViewModel
import ru.practicum.android.diploma.vacancy.ui.VacancyDetailViewModel

val viewModelModule = module {
    factory { VacancyDetailViewModel(get(), get(), get()) }

    factory { SearchViewModel(get(), get(), androidContext()) }

    factory {
        FavoritesViewModel(get())
    }

    factory { IndustryViewModel(get(), get()) }

    factory { FilterViewModel(get()) }

    factory { RegionViewModel() }
}
