package ru.practicum.android.diploma.main.ui.states

sealed interface ErrorType {
    object Network : ErrorType
    object Server : ErrorType
    object Unknown : ErrorType
}
