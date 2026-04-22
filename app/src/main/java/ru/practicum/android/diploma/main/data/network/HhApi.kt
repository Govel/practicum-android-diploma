package ru.practicum.android.diploma.main.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.filter.industry.data.dto.FilterIndustryResponse
import ru.practicum.android.diploma.filter.workplace.data.FilterAreasResponse
import ru.practicum.android.diploma.main.data.dto.VacanciesResponse
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailResponse

interface HhApi {
    @GET("vacancies")
    suspend fun getVacancies(
        @Query("area") area: Int?,
        @Query("industry") industry: Int? = null,
        @Query("text") text: String? = null,
        @Query("salary") salary: Int? = null,
        @Query("page") page: Int? = 1,
        @Query("only_with_salary") onlyWithSalary: Boolean? = false
    ): VacanciesResponse

    @GET("areas")
    suspend fun getAreas(): List<FilterAreasResponse>

    @GET("industries")
    suspend fun getIndustries(): List<FilterIndustryResponse>

    @GET("vacancies/{id}")
    suspend fun getVacancyById(@Path("id") id: String): VacancyDetailResponse
}
