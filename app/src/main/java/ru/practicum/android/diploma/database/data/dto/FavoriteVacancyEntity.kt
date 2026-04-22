package ru.practicum.android.diploma.database.data.dto

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_vacancy_table",
    indices = [Index(value = ["id"], unique = true)]
)
data class FavoriteVacancyEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val salary: String?,
    val address: String?,
    val experience: String?,
    val schedule: String?,
    val employment: String?,
    val contacts: String?,
    val description: String,
    val employer: String,
    val area: String,
    val skills: String,
    val url: String,
    val industry: String
)
