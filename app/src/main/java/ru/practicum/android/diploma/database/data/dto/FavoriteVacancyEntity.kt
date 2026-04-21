package ru.practicum.android.diploma.database.data.dto

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.practicum.android.diploma.vacancy.data.dto.Address
import ru.practicum.android.diploma.vacancy.data.dto.Contacts
import ru.practicum.android.diploma.vacancy.data.dto.Employer
import ru.practicum.android.diploma.vacancy.data.dto.Employment
import ru.practicum.android.diploma.vacancy.data.dto.Experience
import ru.practicum.android.diploma.vacancy.data.dto.FilterArea
import ru.practicum.android.diploma.vacancy.data.dto.FilterIndustry
import ru.practicum.android.diploma.vacancy.data.dto.Salary
import ru.practicum.android.diploma.vacancy.data.dto.Schedule

@Entity(
    tableName = "favorite_vacancy_table",
    indices = [Index(value = ["vacancyId"], unique = true)]
)
data class FavoriteVacancyEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val salary: Salary?,
    val address: Address?,
    val experience: Experience?,
    val schedule: Schedule?,
    val employment: Employment?,
    val contacts: Contacts?,
    val description: String,
    val employer: Employer,
    val area: FilterArea,
    val skills: List<String>,
    val url: String,
    val industry: FilterIndustry
)
