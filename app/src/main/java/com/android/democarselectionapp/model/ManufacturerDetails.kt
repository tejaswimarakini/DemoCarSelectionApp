package com.android.democarselectionapp.model

import com.google.gson.annotations.SerializedName

data class ManufacturerDetails(
    @SerializedName("page") var page: Int,
    @SerializedName("pageSize") var pageSize: Int,
    @SerializedName("totalPageCount") var totalPageCount: Int,
    @SerializedName("wkda") var wkda: LinkedHashMap<String, String>
)

data class wkd(var key: String, var value: String)

data class WKDA(@SerializedName("wkda") var wkda: LinkedHashMap<String, String>)
