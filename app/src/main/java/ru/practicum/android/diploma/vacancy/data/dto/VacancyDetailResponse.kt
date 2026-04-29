package ru.practicum.android.diploma.vacancy.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.main.data.dto.NetworkResponse
import ru.practicum.android.diploma.main.data.dto.VacancySalary

data class VacancyDetailResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("salary") val salary: VacancySalary,
    @SerializedName("address") val addressDto: AddressDto?,
    @SerializedName("experience") val experience: ExperienceDto?,
    @SerializedName("scheduleDto") val scheduleDto: ScheduleDto?,
    @SerializedName("employment") val employmentDto: EmploymentDto?,
    @SerializedName("contacts") val contactsDto: ContactsDto?,
    @SerializedName("employer") val employerDto: EmployerDto,
    @SerializedName("area") val area: FilterAreaDto,
    @SerializedName("skills") val skills: List<String>,
    @SerializedName("url") val url: String,
    @SerializedName("industry") val industry: FilterIndustryDto,
) : NetworkResponse()
