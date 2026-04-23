package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.main.data.impl.VacancyRepositoryImpl
import ru.practicum.android.diploma.main.domain.api.VacanciesRepository

val repositoryModule = module {
    single<VacanciesRepository> {
        VacancyRepositoryImpl(get())
    }
}
