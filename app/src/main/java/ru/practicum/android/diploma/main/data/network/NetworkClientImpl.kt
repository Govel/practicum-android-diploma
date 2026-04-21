package ru.practicum.android.diploma.main.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.main.data.dto.NetworkResponse
import ru.practicum.android.diploma.main.data.model.VacancyFilter

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
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true ||
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true ||
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true
    }

    private suspend fun apiCall(
        doRequest: suspend () -> NetworkResponse
    ): NetworkResponse {
        if (!isConnected()) {
            return NetworkResponse().apply { resultCode = NO_CONNECTION }
        }
        return withContext(Dispatchers.IO) {
            try {
                val result: NetworkResponse = doRequest()
                result.apply { resultCode = OK_RESULT }
            } catch (ex: HttpException) {
                Log.e("error", "Error: ${ex.message}")
                NetworkResponse().apply { resultCode = BAD_REQUEST }
            }
        }
    }

    override suspend fun doRequestVacancies(filter: VacancyFilter): NetworkResponse {
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

    override suspend fun doRequestVacancyById(id: Int): NetworkResponse {
        return apiCall { hhApi.getVacancyById(id = id) }
    }

    companion object {
        const val OK_RESULT = 200
        const val BAD_REQUEST = 400
        const val NO_CONNECTION = -1
    }

}
