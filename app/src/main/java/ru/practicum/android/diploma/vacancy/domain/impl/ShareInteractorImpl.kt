package ru.practicum.android.diploma.vacancy.domain.impl

import ru.practicum.android.diploma.vacancy.domain.api.ShareInteractor
import ru.practicum.android.diploma.vacancy.domain.api.ShareRepository

class ShareInteractorImpl(val shareRepository: ShareRepository) : ShareInteractor {
    override fun shareLink(dataUrl: String) {
        if (dataUrl.isNotBlank()) {
            shareRepository.shareLink(dataUrl)
        }
    }

    override fun sharePhone(phone: String) {
        return shareRepository.sharePhone(phone)
    }

    override fun shareEmail(email: String) {
        return shareRepository.shareEmail(email)
    }
}
