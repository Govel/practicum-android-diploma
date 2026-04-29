package ru.practicum.android.diploma.vacancy.domain.api

interface ShareInteractor {
    fun shareLink(dataUrl: String)

    fun sharePhone(phone: String)

    fun shareEmail(email: String)
}
