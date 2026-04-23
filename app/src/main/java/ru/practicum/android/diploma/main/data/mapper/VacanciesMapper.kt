package ru.practicum.android.diploma.main.data.mapper

import ru.practicum.android.diploma.main.data.dto.VacancyCardDto
import ru.practicum.android.diploma.main.data.dto.VacancyCardSalary
import ru.practicum.android.diploma.main.domain.models.VacancyCard

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

    private fun salaryDtoToSalaryModelConverter(salary: VacancyCardSalary?): String {
        var result: String
        val from = formatSalary(salary?.from)
        val to = formatSalary(salary?.to)
        val currency = formatCurrency(salary?.currency)
        if (from == "" && to == "") return ""
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
}
