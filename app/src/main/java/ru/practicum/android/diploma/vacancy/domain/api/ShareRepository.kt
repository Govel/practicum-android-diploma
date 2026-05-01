package ru.practicum.android.diploma.vacancy.domain.api

interface ShareRepository {
    fun shareLink(dataUrl: String)

    fun sharePhone(phone: String)

    fun shareEmail(email: String)
}
