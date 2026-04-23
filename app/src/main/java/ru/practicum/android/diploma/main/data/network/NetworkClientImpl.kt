package ru.practicum.android.diploma.main.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.main.data.dto.NetworkResponse
import ru.practicum.android.diploma.main.data.model.VacancyFilterRequest

class NetworkClientImpl(
    private val hhApi: HhApi,
    private val context: Context
) : NetworkClient {
    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        val hasInternet = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        val isPreferredTransport = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true ||
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true ||
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true
        return hasInternet ?: false && isPreferredTransport
    }

    private suspend fun apiCall(
        doRequest: suspend () -> Any?
    ): NetworkResponse {
        if (!isConnected()) {
            return NetworkResponse().apply { resultCode = NetworkResponse.NO_CONNECTION }
        }
        return withContext(Dispatchers.IO) {
            try {
                val result = doRequest()
                NetworkResponse().apply {
                    resultCode = NetworkResponse.OK_RESULT
                    data = result
                }
            } catch (ex: HttpException) {
                Log.e("error", "Error: ${ex.message}")
                NetworkResponse().apply { resultCode = NetworkResponse.BAD_REQUEST }
            }
        }
    }

    override suspend fun doRequestVacancies(filter: VacancyFilterRequest): NetworkResponse {
        return apiCall {
            hhApi.getVacancies(
                area = filter.area,
                industry = filter.industry,
                text = filter.text,
                salary = filter.salary,
                page = filter.page,
                onlyWithSalary = filter.onlyWithSalary
            )
        }
    }

    override suspend fun doRequestAreas(): NetworkResponse {
        return apiCall { hhApi.getAreas() }
    }

    override suspend fun doRequestIndustries(): NetworkResponse {
        return apiCall { hhApi.getIndustries() }
    }

    override suspend fun doRequestVacancyById(id: String): NetworkResponse {
        return apiCall { hhApi.getVacancyById(id = id) }
    }
}
