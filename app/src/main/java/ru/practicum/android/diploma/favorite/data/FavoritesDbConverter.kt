package ru.practicum.android.diploma.favorite.data

import ru.practicum.android.diploma.database.data.dto.FavoriteVacancyEntity
import ru.practicum.android.diploma.favorite.domain.models.VacancyCard
import kotlin.String

class FavoritesDbConverter() {

    fun map(vacancy: VacancyCard): FavoriteVacancyEntity {
        return FavoriteVacancyEntity(
            id = vacancy.id,
            name = vacancy.name,
            salary = (vacancy.salary ?: "") as String?,
            address = null,
            experience = null,
            schedule = null,
            employment = null,
            contacts = null,
            description = "",
            employer = "",
            area = "",
            skills = "",
            url = vacancy.logo ?: "",
            industry = ""
        )
    }

    fun map(vacancy: FavoriteVacancyEntity): VacancyCard {
        return VacancyCard(
            id = vacancy.id,
            name = vacancy.name,
            company = "",
            city = "",
            salary = null,
            logo = vacancy.url ?: ""
        )
    }
}
