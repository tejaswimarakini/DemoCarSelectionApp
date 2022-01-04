package com.android.democarselectionapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.democarselectionapp.model.wkd
import com.android.democarselectionapp.network.NetworkRepository

class CarModelPagingSource(
    private val networkRepository: NetworkRepository,
    private val manufacturer: String
) : PagingSource<Int, wkd>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, wkd> {
        return try {
            val response = networkRepository.getModelData(manufacturer)
            val wkdList = mutableListOf<wkd>()
            for (obj in response.wkda) {
                val wkd = wkd(obj.key, obj.value)
                wkdList.add(wkd)
            }

            LoadResult.Page(
                data = wkdList,
                prevKey = null,
                nextKey = null // Because this api contains only 1 page, no need to plus the page number
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, wkd>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}