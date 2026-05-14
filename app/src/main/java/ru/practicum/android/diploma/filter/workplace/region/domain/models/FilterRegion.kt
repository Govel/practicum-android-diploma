package ru.practicum.android.diploma.filter.workplace.region.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterRegion(
    val id: Int,
    val name: String
) : Parcelable
