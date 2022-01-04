package com.android.democarselectionapp.network

import com.android.democarselectionapp.BuildConfig
import com.android.democarselectionapp.model.ManufacturerDetails
import com.android.democarselectionapp.model.WKDA
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APIClient {
    private val apiInterface: APiInterface
    private val retrofit: Retrofit
    private val DEFAULT_TIMEOUT = 5L
    private val okHttpClient: OkHttpClient

    init {
        val logging = Interceptor { chain ->
            val request = chain.request()
            chain.proceed(request)
        }
        okHttpClient = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
        apiInterface = retrofit.create(APiInterface::class.java)
    }

    suspend fun getManufacturerData(
        page: Int
    ): ManufacturerDetails {
        return apiInterface.getManufacturerData(BuildConfig.API_KEY, page, pageSize = 15)
    }

    suspend fun getModelData(
        manufacturer: String
    ): ManufacturerDetails {
        return apiInterface.getModelDetails(BuildConfig.API_KEY, manufacturer)
    }

    fun getModelYearData(
        manufacturer: Int,
        mainType: String
    ): Call<WKDA> {
        return apiInterface.getModelYearData(BuildConfig.API_KEY, manufacturer, mainType)
    }
}