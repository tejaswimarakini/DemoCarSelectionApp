package com.android.democarselectionapp.viewmodel

import androidx.lifecycle.*
import androidx.paging.*
import androidx.ui.text.toLowerCase
import com.android.democarselectionapp.model.WKDA
import com.android.democarselectionapp.network.NetworkRepository
import com.android.democarselectionapp.paging.CarModelPagingSource
import com.android.democarselectionapp.paging.ManufacturePagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.util.*

class MainViewModel : ViewModel() {

    private val networkRepository = NetworkRepository()
    private var manufacturerLiveData: MutableLiveData<String> = MutableLiveData<String>()
    private var selectedCarNameLiveData: MutableLiveData<String> = MutableLiveData<String>()
    private var searchQueryLiveData: MutableLiveData<String> = MutableLiveData<String>()
    private var builtYearLiveData: MutableLiveData<WKDA> = MutableLiveData<WKDA>()
    private var mainTypeLiveData: MutableLiveData<String> = MutableLiveData<String>()
    private var selectedBuiltYearLiveData: MutableLiveData<String> = MutableLiveData<String>()

    private val manFacturesData = Pager(PagingConfig(pageSize = 6)) {
        ManufacturePagingSource(networkRepository)
    }.flow.cachedIn(viewModelScope)

    private val carModels = Pager(PagingConfig(pageSize = 2147483647)) {
        CarModelPagingSource(networkRepository, manufacturerLiveData.value!!)
    }.flow

    private val queryFlow = MutableStateFlow("init_query")
    private val filteredCarModels = carModels.combine(queryFlow) { pagingData, query ->
        pagingData.filter { wkd ->
            wkd.value.lowercase(Locale.getDefault()).contains(
                searchQueryLiveData.value?.lowercase().toString()
            )
        }
    }

    fun getManuFacturesData() = manFacturesData

    fun getCarModelsData() = carModels

    fun getFilteredCarModelsData() = filteredCarModels

    fun getSelectedCarNameLiveData(): LiveData<String> = selectedCarNameLiveData

    fun getBuiltYearLiveData() = builtYearLiveData

    fun getMainTypeCarLiveData(): LiveData<String> = mainTypeLiveData

    fun getSelectedBuiltYearLiveData(): LiveData<String> = selectedBuiltYearLiveData

    fun setSelectedBuiltYearLiveData(year: String) {
        selectedBuiltYearLiveData.value = year
    }

    fun setMainTypeCarLiveData(model: String) {
        mainTypeLiveData.value = model
    }

    fun setSearchQueryLiveData(searchQuery: String) {
        searchQueryLiveData.value = searchQuery
    }

    fun setManufacturerLiveData(carId: String) {
        manufacturerLiveData.value = carId
    }

    fun setSelectedCarNameLiveData(carName: String) {
        selectedCarNameLiveData.value = carName
    }

    fun getBuiltDates() {
        networkRepository.getModelYear(
            manufacturerLiveData.value!!.toInt(),
            mainTypeLiveData.value.toString(),
            builtYearLiveData
        )
    }
}