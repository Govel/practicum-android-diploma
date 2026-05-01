package ru.practicum.android.diploma.main.data.model

enum class VacanciesSearchState(val state: String) {
    Error("Error"),
    Empty("Empty"),
    NoConnection("Проверьте подключение к интернету")
}
