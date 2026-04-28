package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.main.data.impl.VacanciesRepositoryImpl
import ru.practicum.android.diploma.main.domain.api.VacanciesRepository
import ru.practicum.android.diploma.vacancy.data.impl.VacancyDetailRepositoryImpl
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailRepository

val repositoryModule = module {
    factory<VacanciesRepository> {
        VacanciesRepositoryImpl(get())
    }
    factory<VacancyDetailRepository> {
        VacancyDetailRepositoryImpl(get())
    }
}
