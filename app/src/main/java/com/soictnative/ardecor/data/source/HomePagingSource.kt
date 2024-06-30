package com.soictnative.ardecor.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.soictnative.ardecor.data.api.ApiService
import com.soictnative.ardecor.data.dto.Product

class HomePagingSource(
    private val apiService: ApiService,
) : PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition
            ?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: 1
        return try {
            val response = apiService.getAllProducts(page = page)
            val products = response.data.distinctBy { it.id }

            LoadResult.Page(
                data = products,
                nextKey = if (page == response.totalPages) null else page + 1,
                prevKey = if (page == 1) null else page - 1,
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(
                throwable = e
            )
        }
    }
}