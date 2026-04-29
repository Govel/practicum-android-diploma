package ru.practicum.android.diploma.main.domain.models

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val totalFound: Int? = null
) {
    class Success<T>(data: T, totalFound: Int) : Resource<T>(data, totalFound = totalFound)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}
