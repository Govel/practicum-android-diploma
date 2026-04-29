package ru.practicum.android.diploma.main.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.database.data.dto.FavoriteVacancyEntity
import ru.practicum.android.diploma.main.data.dto.VacancyCardDto
import ru.practicum.android.diploma.main.data.dto.VacancySalary
import ru.practicum.android.diploma.main.domain.models.VacancyCard
import ru.practicum.android.diploma.vacancy.data.dto.AddressDto
import ru.practicum.android.diploma.vacancy.data.dto.ContactsDto
import ru.practicum.android.diploma.vacancy.data.dto.EmployerDto
import ru.practicum.android.diploma.vacancy.data.dto.EmploymentDto
import ru.practicum.android.diploma.vacancy.data.dto.ExperienceDto
import ru.practicum.android.diploma.vacancy.data.dto.FilterAreaDto
import ru.practicum.android.diploma.vacancy.data.dto.FilterIndustryDto
import ru.practicum.android.diploma.vacancy.data.dto.PhoneDto
import ru.practicum.android.diploma.vacancy.data.dto.ScheduleDto
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailResponse
import ru.practicum.android.diploma.vacancy.domain.models.AddressEmployer
import ru.practicum.android.diploma.vacancy.domain.models.ContactsEmployer
import ru.practicum.android.diploma.vacancy.domain.models.Employer
import ru.practicum.android.diploma.vacancy.domain.models.Phone
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetail
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

    private fun salaryDtoToSalaryModelConverter(salary: VacancySalary?): String? {
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

    fun mapVacancyDetailDtoToDomain(vacancyDetail: VacancyDetailResponse): VacancyDetail {
        return VacancyDetail(
            id = vacancyDetail.id,
            name = vacancyDetail.name,
            description = vacancyDetail.description,
            salary = salaryDtoToSalaryModelConverter(vacancyDetail.salary),
            address = mapAddressDtoToDomain(vacancyDetail.addressDto),
            experience = vacancyDetail.experience?.name,
            schedule = vacancyDetail.scheduleDto?.name,
            employment = vacancyDetail.employmentDto?.name,
            contacts = mapContactDtoToDomain(vacancyDetail.contactsDto),
            employer = mapEmployerDtoToDomain(vacancyDetail.employerDto),
            area = vacancyDetail.area.name,
            skills = vacancyDetail.skills,
            url = vacancyDetail.url,
            industry = vacancyDetail.industry.name
        )
    }

    private fun mapAddressDtoToDomain(addressDto: AddressDto?): AddressEmployer = AddressEmployer(
        city = addressDto?.city ?: "",
        street = addressDto?.street ?: "",
        building = addressDto?.building ?: "",
        raw = addressDto?.raw ?: ""
    )

    private fun mapContactDtoToDomain(contact: ContactsDto?): ContactsEmployer = ContactsEmployer(
        name = contact?.name ?: "",
        email = contact?.email ?: "",
        phones = mapPhonesDtoToDomain(contact?.phonesDto ?: emptyList())
    )

    private fun mapPhonesDtoToDomain(phonesDto: List<PhoneDto>): List<Phone> {
        return phonesDto.map { phone -> Phone(comment = phone.comment, formatted = phone.formatted) }
    }

    private fun mapEmployerDtoToDomain(employer: EmployerDto): Employer = Employer(
        name = employer.name,
        logo = employer.logo
    )
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
            address = gson.toJson(vacancy.addressDto),
            experience = gson.toJson(vacancy.experience),
            schedule = gson.toJson(vacancy.scheduleDto),
            employment = gson.toJson(vacancy.employmentDto),
            contacts = gson.toJson(vacancy.contactsDto),
            description = vacancy.description,
            employer = gson.toJson(vacancy.employerDto),
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
            salary = gson.fromJson(vacancy.salary, VacancySalary::class.java),
            addressDto = gson.fromJson(vacancy.address, AddressDto::class.java),
            experience = gson.fromJson(vacancy.experience, ExperienceDto::class.java),
            scheduleDto = gson.fromJson(vacancy.schedule, ScheduleDto::class.java),
            employmentDto = gson.fromJson(vacancy.employment, EmploymentDto::class.java),
            contactsDto = gson.fromJson(vacancy.contacts, ContactsDto::class.java),
            description = vacancy.description,
            employerDto = gson.fromJson(vacancy.employer, EmployerDto::class.java),
            area = gson.fromJson(vacancy.area, FilterAreaDto::class.java),
            skills = gson.fromJson(vacancy.skills, object : TypeToken<MutableList<String>>() {}.type),
            url = vacancy.url,
            industry = gson.fromJson(vacancy.industry, FilterIndustryDto::class.java)
        )
    }

    fun mapEntityListToResponse(entityList: List<FavoriteVacancyEntity>): List<VacancyDetailResponse> {
        return entityList.map { entity -> mapEntityToResponse(entity) }
    }

    private fun salaryFromString(salaryString: String?): VacancySalary? {
        return if (!salaryString.isNullOrEmpty()) {
            gson.fromJson(salaryString, VacancySalary::class.java)
        } else {
            null
        }
    }
    private fun getValueCompany(employer: String): String? {
        val value = gson.fromJson(employer, Employer::class.java)
        return value.name ?: null
    }

    private fun getValueCity(city: String?): String? {
        val value = gson.fromJson(city, AddressDto::class.java)
        return value.city ?: null
    }
}
