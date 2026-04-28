package ru.practicum.android.diploma.main.data.mapper

import ru.practicum.android.diploma.main.data.dto.VacancyCardDto
import ru.practicum.android.diploma.main.data.dto.VacancySalary
import ru.practicum.android.diploma.main.domain.models.VacancyCard
import ru.practicum.android.diploma.vacancy.data.dto.AddressDto
import ru.practicum.android.diploma.vacancy.data.dto.ContactsDto
import ru.practicum.android.diploma.vacancy.data.dto.EmployerDto
import ru.practicum.android.diploma.vacancy.data.dto.PhoneDto
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailResponse
import ru.practicum.android.diploma.vacancy.domain.models.AddressEmployer
import ru.practicum.android.diploma.vacancy.domain.models.ContactsEmployer
import ru.practicum.android.diploma.vacancy.domain.models.Employer
import ru.practicum.android.diploma.vacancy.domain.models.Phone
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetail

object VacanciesMapper {

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
}
