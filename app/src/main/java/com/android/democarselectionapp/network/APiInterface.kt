package com.android.democarselectionapp.network

import com.android.democarselectionapp.model.ManufacturerDetails
import com.android.democarselectionapp.model.WKDA
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APiInterface {

    @GET("manufacturer")
    suspend fun getManufacturerData(
        @Query("wa_key") key: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): ManufacturerDetails

    @GET("main-types")
    suspend fun getModelDetails(
        @Query("wa_key") key: String,
        @Query("manufacturer") manufacturer: String,
    ): ManufacturerDetails

    @GET("built-dates")
    fun getModelYearData(
        @Query("wa_key") key: String,
        @Query("manufacturer") manufacturer: Int,
        @Query("main-type") mainType: String
    ): Call<WKDA>
}