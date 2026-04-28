package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.main.ui.SearchViewModel
import ru.practicum.android.diploma.vacancy.ui.VacancyDetailViewModel

val viewModelModule = module {
    factory { SearchViewModel(get()) }
    factory { VacancyDetailViewModel(get()) }

}
