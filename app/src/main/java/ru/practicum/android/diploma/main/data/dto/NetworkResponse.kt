package ru.practicum.android.diploma.main.data.dto

open class NetworkResponse {
    var resultCode = 0
    var data: Any? = null

    companion object {
        const val OK_RESULT = 200
        const val BAD_REQUEST = 400
        const val NO_CONNECTION = -1
    }
}
