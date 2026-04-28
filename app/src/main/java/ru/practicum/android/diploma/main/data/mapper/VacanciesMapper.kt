package ru.practicum.android.diploma.main.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.database.data.dto.FavoriteVacancyEntity
import ru.practicum.android.diploma.main.data.dto.VacancyCardDto
import ru.practicum.android.diploma.main.data.dto.VacancyCardSalary
import ru.practicum.android.diploma.main.domain.models.VacancyCard
import ru.practicum.android.diploma.vacancy.data.dto.Address
import ru.practicum.android.diploma.vacancy.data.dto.Contacts
import ru.practicum.android.diploma.vacancy.data.dto.Employer
import ru.practicum.android.diploma.vacancy.data.dto.Employment
import ru.practicum.android.diploma.vacancy.data.dto.Experience
import ru.practicum.android.diploma.vacancy.data.dto.FilterArea
import ru.practicum.android.diploma.vacancy.data.dto.FilterIndustry
import ru.practicum.android.diploma.vacancy.data.dto.Salary
import ru.practicum.android.diploma.vacancy.data.dto.Schedule
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailResponse
import kotlin.Int
import kotlin.String

object VacanciesMapper {

    private val gson = Gson()
    private const val SEPARATE_COUNT_NUMBER = 3
    private fun mapDtoToDomain(vacancyCardDto: VacancyCardDto): VacancyCard {
        return VacancyCard(
            id = vacancyCardDto.id,
            name = vacancyCardDto.name,
            company = vacancyCardDto.company,
            city = vacancyCardDto.city,
            salary = salaryDtoToSalaryModelConverter(vacancyCardDto.salary),
            logo = vacancyCardDto.logo
        )
    }

    fun mapDtoListToDomain(dtoList: List<VacancyCardDto>): List<VacancyCard> {
        return dtoList.map { vacancy -> mapDtoToDomain(vacancy) }
    }

    private fun salaryDtoToSalaryModelConverter(salary: VacancyCardSalary?): String? {
        var result: String
        val from = formatSalary(salary?.from)
        val to = formatSalary(salary?.to)
        val currency = formatCurrency(salary?.currency)
        if (from == "" && to == "") return null
        if (from != "") {
            result = "От $from"
            if (to != "") {
                result += " до $to"
            }
        } else {
            result = to
        }
        result += " $currency"
        return result
    }

    private fun formatSalary(salary: Int?): String {
        if (salary == null || salary < 0) return ""
        return salary.toString()
            .reversed()
            .chunked(SEPARATE_COUNT_NUMBER)
            .joinToString("\u00A0")
            .reversed()
    }

    private fun formatCurrency(currency: String?): String {
        if (currency == "" || currency == null) return ""
        return when (currency) {
            "RUB" -> "₽"
            "RUR" -> "₽"
            "EUR" -> "€"
            "USD" -> "$"
            "KZT" -> "₸"
            "KGT" -> "₸"
            "GEL" -> "₾"
            else -> currency
        }
    }

    fun mapEntityToDomain(entity: FavoriteVacancyEntity): VacancyCard {
        return VacancyCard(
            id = entity.id,
            name = entity.name,
            company = getValueCompany(entity.employer),
            city = getValueCity(entity.address),
            salary = salaryDtoToSalaryModelConverter(salaryFromString(entity.salary)),
            logo = entity.url
        )
    }

    fun mapEntityListToDomain(entityList: List<FavoriteVacancyEntity>): List<VacancyCard> {
        return entityList.map { entity -> mapEntityToDomain(entity) }
    }

    fun mapResponseToEntity(vacancy: VacancyDetailResponse): FavoriteVacancyEntity {
        return FavoriteVacancyEntity(
            id = vacancy.id,
            name = vacancy.name,
            salary = gson.toJson(vacancy.salary),
            address = gson.toJson(vacancy.address),
            experience = gson.toJson(vacancy.experience),
            schedule = gson.toJson(vacancy.schedule),
            employment = gson.toJson(vacancy.employment),
            contacts = gson.toJson(vacancy.contacts),
            description = vacancy.description,
            employer = gson.toJson(vacancy.employer),
            area = gson.toJson(vacancy.area),
            skills = gson.toJson(vacancy.skills),
            url = vacancy.url,
            industry = gson.toJson(vacancy.industry)
        )
    }

    fun mapResponseListToDomain(responseList: List<VacancyDetailResponse>): List<FavoriteVacancyEntity> {
        return responseList.map { vacancy -> mapResponseToEntity(vacancy) }
    }

    private fun mapEntityToResponse(vacancy: FavoriteVacancyEntity): VacancyDetailResponse {
        return VacancyDetailResponse(
            id = vacancy.id,
            name = vacancy.name,
            salary = gson.fromJson(vacancy.salary, Salary::class.java),
            address = gson.fromJson(vacancy.address, Address::class.java),
            experience = gson.fromJson(vacancy.experience, Experience::class.java),
            schedule = gson.fromJson(vacancy.schedule, Schedule::class.java),
            employment = gson.fromJson(vacancy.employment, Employment::class.java),
            contacts = gson.fromJson(vacancy.contacts, Contacts::class.java),
            description = vacancy.description,
            employer = gson.fromJson(vacancy.employer, Employer::class.java),
            area = gson.fromJson(vacancy.area, FilterArea::class.java),
            skills = gson.fromJson(vacancy.skills, object : TypeToken<MutableList<String>>() {}.type),
            url = vacancy.url,
            industry = gson.fromJson(vacancy.industry, FilterIndustry::class.java)
        )
    }

    fun mapEntityListToResponse(entityList: List<FavoriteVacancyEntity>): List<VacancyDetailResponse> {
        return entityList.map { entity -> mapEntityToResponse(entity) }
    }

    private fun salaryFromString(salaryString: String?): VacancyCardSalary? {
        return if (!salaryString.isNullOrEmpty()) {
            gson.fromJson(salaryString, VacancyCardSalary::class.java)
        } else {
            null
        }
    }
    private fun getValueCompany(employer: String): String? {
        val value = gson.fromJson(employer, Employer::class.java)
        return value.name ?: null
    }

    private fun getValueCity(city: String?): String? {
        val value = gson.fromJson(city, Address::class.java)
        return value.city ?: null
    }
}
