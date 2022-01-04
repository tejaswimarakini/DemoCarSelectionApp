package com.android.democarselectionapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.democarselectionapp.model.wkd
import com.android.democarselectionapp.network.NetworkRepository
import java.lang.Exception

class ManufacturePagingSource(
    private val networkRepository: NetworkRepository
) : PagingSource<Int, wkd>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, wkd> {
        return try {
            val pageNumber = params.key ?: 0
            val response = networkRepository.getManufacturerData(pageNumber)
            val list = mutableListOf<wkd>()
            for (obj in response.wkda) {
                val wkd = wkd(obj.key, obj.value)
                list.add(wkd)
            }

            LoadResult.Page(
                data = if (response.wkda.isNotEmpty()) list else mutableListOf(),
                prevKey = if (response.wkda.isEmpty()) null else pageNumber.minus(1),
                nextKey = if (response.wkda.isEmpty()) null else pageNumber.plus(1)
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
