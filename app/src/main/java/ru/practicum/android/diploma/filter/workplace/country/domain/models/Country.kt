package ru.practicum.android.diploma.filter.workplace.country.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    val id: Int,
    val name: String
) : Parcelable
