package ru.practicum.android.diploma.vacancy.data.impl

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import androidx.core.net.toUri
import ru.practicum.android.diploma.vacancy.domain.api.ShareRepository

class ShareRepositoryImpl(private val context: Context) : ShareRepository {
    override fun shareLink(dataUrl: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_TEXT, dataUrl)
        shareIntent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(shareIntent)
        } catch (ex: ActivityNotFoundException) {
            Log.e(LOG_TAG, LOG_MESSAGE, ex)
        }
    }

    override fun sharePhone(phone: String) {
        val clearPhone = phone.replace(Regex("[^+\\d]"), "")
        val dialIntent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$clearPhone".toUri()
            flags = FLAG_ACTIVITY_NEW_TASK
        }
        try {
            context.startActivity(dialIntent)
        } catch (ex: ActivityNotFoundException) {
            Log.e(LOG_TAG, LOG_MESSAGE, ex)
        }
    }

    override fun shareEmail(email: String) {
        val writeContactIntent = Intent(Intent.ACTION_SENDTO)
        writeContactIntent.data = "mailto:$email".toUri()
        try {
            context.startActivity(writeContactIntent.addFlags(FLAG_ACTIVITY_NEW_TASK))
        } catch (ex: ActivityNotFoundException) {
            Log.e(LOG_TAG, LOG_MESSAGE, ex)
        }
    }

    companion object {
        const val LOG_TAG = "ShareRepositoryImpl"
        const val LOG_MESSAGE = "Error in the share action"
    }
}
