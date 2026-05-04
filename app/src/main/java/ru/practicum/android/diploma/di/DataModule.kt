package ru.practicum.android.diploma.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.svg.SvgDecoder
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.database.AppDatabase
import ru.practicum.android.diploma.filter.data.storage.FilterSettingsStorage
import ru.practicum.android.diploma.main.data.network.HhApi
import ru.practicum.android.diploma.main.data.network.NetworkClient
import ru.practicum.android.diploma.main.data.network.NetworkClientImpl

private const val HH_BASE_URL = "https://android-diploma.education-services.ru"
private const val DB_NAME = "database.db"
private const val API_TOKEN = "Bearer ${BuildConfig.API_ACCESS_TOKEN}"

val dataModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, DB_NAME).build()
    }

    single { AppDatabase.getInstance(androidContext()) }

    single<HhApi> {
        Retrofit.Builder().baseUrl(HH_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
            .create(HhApi::class.java)
    }

    single {
        OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request().newBuilder().header("User-Agent", "Mozilla/5.0").build()
            chain.proceed(request)
        }.build()
    }

    single<ImageLoader> {
        ImageLoader.Builder(androidContext()).components {
            add(SvgDecoder.Factory())
            add(
                OkHttpNetworkFetcherFactory(
                    callFactory = {
                        OkHttpClient.Builder().addInterceptor { chain ->
                            val request =
                                chain.request().newBuilder().header("User-Agent", "Mozilla/5.0").build()
                            chain.proceed(request)
                        }.build()
                    }
                )
            )
        }.build()
    }

    single<NetworkClient> { NetworkClientImpl(get(), androidContext(), API_TOKEN) }

    single<SharedPreferences> {
        androidContext().getSharedPreferences("filters", Context.MODE_PRIVATE)
    }

    single<FilterSettingsStorage> {
        FilterSettingsStorage(get())
    }
}
