package ru.practicum.android.diploma.vacancy.domain.impl

import ru.practicum.android.diploma.vacancy.domain.api.ShareInteractor
import ru.practicum.android.diploma.vacancy.domain.api.ShareRepository

class ShareInteractorImpl(val shareRepository: ShareRepository) : ShareInteractor {
    override fun shareLink(dataUrl: String) {
        return shareRepository.shareLink(dataUrl)
    }
}
