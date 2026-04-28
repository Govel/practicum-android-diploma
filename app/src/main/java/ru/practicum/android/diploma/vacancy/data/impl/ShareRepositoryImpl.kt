package ru.practicum.android.diploma.vacancy.data.impl

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import ru.practicum.android.diploma.vacancy.domain.api.ShareRepository

class ShareRepositoryImpl(private val context: Context) : ShareRepository {
    override fun shareLink(dataUrl: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("plain/text")
        shareIntent.putExtra(Intent.EXTRA_TEXT, dataUrl)
        shareIntent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(shareIntent)
        } catch (ex: ActivityNotFoundException) {
            Log.e("ShareRepositoryImpl", "Error in the share action", ex)
        }
    }
}
