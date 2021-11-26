package com.merajavier.cineme.movies.upcoming

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.merajavier.cineme.common.ErrorResponse
import com.merajavier.cineme.common.TMDBApiResult
import com.merajavier.cineme.movies.MovieDataItem
import com.merajavier.cineme.movies.MoviesResponse
import com.merajavier.cineme.network.repositories.NetworkMoviesRepositoryInterface

class UpcomingMoviesPagingSource(
    private val apiInterface: NetworkMoviesRepositoryInterface
) : PagingSource<Int, MovieDataItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDataItem> {
        return try {
            val page = params.key ?: 1
            when (val result = apiInterface.getUpcoming(page)) {
                is TMDBApiResult.Success -> {
                    val response = result.data as MoviesResponse
                    LoadResult.Page(
                        response.movies,
                        if (page == 1) null else page - 1,
                        if (response.movies.isEmpty()) null else page.inc()
                    )
                }
                is TMDBApiResult.Failure -> {
                    val failureResponse = result.data as ErrorResponse
                    LoadResult.Error(Exception(failureResponse.statusMessage))
                }
                is TMDBApiResult.Error -> {
                    LoadResult.Error(Exception(result.message))
                }
                TMDBApiResult.Init -> LoadResult.Page(
                    listOf(), null, null
                )
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieDataItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}