package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.main.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.main.domain.impl.VacanciesInteractorImpl

val interactorModule = module {
    factory <VacanciesInteractor> {
        VacanciesInteractorImpl(get())
    }
}
