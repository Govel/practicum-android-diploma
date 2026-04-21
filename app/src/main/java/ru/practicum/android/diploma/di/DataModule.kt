package ru.practicum.android.diploma.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.database.AppDatabase
import ru.practicum.android.diploma.main.data.network.HhApi
import ru.practicum.android.diploma.main.data.network.NetworkClient
import ru.practicum.android.diploma.main.data.network.NetworkClientImpl

private const val HH_BASE_URL = "https://android-diploma.education-services.ru"

val dataModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }

    single {
        get<AppDatabase>().favoriteVacancyDao()
    }

    single<HhApi> {
        Retrofit.Builder()
            .baseUrl(HH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HhApi::class.java)
    }

    single<NetworkClient> { NetworkClientImpl(get(), androidContext()) }
}
