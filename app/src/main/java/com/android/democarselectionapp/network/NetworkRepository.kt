package com.android.democarselectionapp.network

import androidx.lifecycle.MutableLiveData
import com.android.democarselectionapp.model.ManufacturerDetails
import com.android.democarselectionapp.model.WKDA
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkRepository {

    private var apiClient = APIClient()

    suspend fun getManufacturerData(
        page: Int
    ): ManufacturerDetails {
        return apiClient.getManufacturerData(page)
    }

    suspend fun getModelData(
        manufacturer: String
    ): ManufacturerDetails {
        return apiClient.getModelData(manufacturer)
    }

    fun getModelYear(
        manufacturer: Int,
        mainType: String,
        modelYearLiveData: MutableLiveData<WKDA>
    ) {
        val response = apiClient.getModelYearData(manufacturer, mainType)
        response.enqueue(object : Callback<WKDA> {
            override fun onResponse(call: Call<WKDA>, response: Response<WKDA>) {
                modelYearLiveData.value = response.body()
            }

            override fun onFailure(call: Call<WKDA>, t: Throwable) {
                throw Exception("No Built Dates are found '\n' ${t.localizedMessage}")
            }

        })
    }

}